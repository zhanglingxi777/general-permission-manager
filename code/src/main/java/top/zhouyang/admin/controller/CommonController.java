package top.zhouyang.admin.controller;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zhouyang.common.domain.AjaxResult;
import top.zhouyang.common.utils.FileUtils;
import top.zhouyang.common.utils.StringUtils;
import top.zhouyang.framework.config.ThreadLocalConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ThreadLocalConfig threadLocalConfig;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 是否成功
     */
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            String filePath = fileUtils.uploadFile(is);
            filePath = filePath.replace(fileUtils.profile, "");
            if (!filePath.startsWith("/")) {
                filePath = "/" + filePath;
            }
            return AjaxResult.success("上传成功!", filePath);
        } catch (IOException e) {
            log.error("上传文件时发生异常,异常信息: {}", e.getMessage());
            return AjaxResult.error("上传失败");
        }
    }

    /**
     * 上传头像
     *
     * @param file 头像文件
     * @param username 用户名
     * @return 是否成功
     */
    @PostMapping("/upload/avatar/{username}")
    public AjaxResult uploadAvatar(MultipartFile file, @PathVariable(required = false) String username) {
        try (InputStream is = file.getInputStream()) {
            if (StringUtils.isBlank(username)) {
                Map map = threadLocalConfig.get();
                username = map.get("username").toString();
            }
            String filePath = fileUtils.uploadFile(is, "avatar", username);
            filePath = filePath.replace(fileUtils.profile, "");
            if (!filePath.startsWith("/")) {
                filePath = "/" + filePath;
            }
            return AjaxResult.success("上传成功!", filePath);
        } catch (IOException e) {
            log.error("上传头像时发生异常,异常信息: {}", e.getMessage());
            return AjaxResult.error("上传失败");
        }
    }
}
