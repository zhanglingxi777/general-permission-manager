package top.zhouyang.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.zhouyang.common.domain.AjaxResult;

@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 上传文件
     * @param file 文件
     * @return 是否成功
     */
    public AjaxResult upload(MultipartFile file) {
        return null;
    }


}
