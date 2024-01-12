package top.zhouyang.common.utils;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class FileUtils {
    @Value("${custom.profile}")
    public String profile;

    /**
     * 上传文件
     *
     * @param is       文件流
     * @param prefix   文件前缀路径
     * @param fileName 文件名
     * @return 文件全路径
     */
    public String uploadFile(InputStream is, String prefix, String fileName) throws IOException {
        // 获取文件类型
        ByteArrayInputStream bis = new ByteArrayInputStream(IoUtil.readBytes(is));
        // 根据文件流的头部信息获得文件类型 注意此方法会读取头部64个bytes，
        // 造成此流接下来读取时缺少部分bytes 因此如果想复用此流，流需支持InputStream.reset()方法
        String type = FileTypeUtil.getType(bis);
        bis.reset();
        String filePath = profile + prefix + "/" + fileName + "." + type;
        FileUtil.writeBytes(IoUtil.readBytes(bis), filePath);
        return filePath;
    }

    /**
     * 上传文件
     *
     * @param is     文件流
     * @param prefix 文件前缀路径
     * @return 文件全路径
     */
    public String uploadFile(InputStream is, String prefix) throws IOException {
        return uploadFile(is, prefix, UUID.randomUUID().toString());
    }

    /**
     * 上传文件
     *
     * @param is 文件流
     * @return 文件全路径
     */
    public String uploadFile(InputStream is) throws IOException {
        // 获取文件类型
        return uploadFile(is, "file");
    }
}
