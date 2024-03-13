package top.zhanglingxi.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户导出VO
 */
@Data
@ContentRowHeight(25)
@HeadRowHeight(30)
@ColumnWidth(25)
public class UserImportVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String username;

    /**
     * 电话
     */
    @ExcelProperty("电话")
    private String phone;

    /**
     * 邮箱
     */
    @ExcelProperty("邮箱")
    private String email;

    /**
     * 真实姓名
     */
    @ExcelProperty("真实姓名")
    private String realName;

    /**
     * 性别（男女）
     */
    @ExcelProperty("性别（男女）")
    private String gender;
}
