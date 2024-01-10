package top.zhouyang.common.utils;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class FileUtils {
    @Value("${custom.profile}")
    private String profile;

    /**
     * 上传文件
     *
     * @param is       文件流
     * @param prefix   文件前缀路径
     * @param fileName 文件名
     */
    public void uploadFile(InputStream is, String prefix, String fileName) {
        // 获取文件类型
        String type = FileTypeUtil.getType(is);
        String filePath = prefix + prefix + "/" + fileName + "." + type;
        FileUtil.writeFromStream(is, filePath);
    }

    /**
     * 上传文件
     *
     * @param is     文件流
     * @param prefix 文件前缀路径
     */
    public void uploadFile(InputStream is, String prefix) {
        // 获取文件类型
        String type = FileTypeUtil.getType(is);
        uploadFile(is, prefix, UUID.randomUUID() + "." + type);
    }

    /**
     * 上传文件
     *
     * @param is 文件流
     */
    public void uploadFile(InputStream is) {
        // 获取文件类型
        String type = FileTypeUtil.getType(is);
        uploadFile(is, "/file");
    }
}
