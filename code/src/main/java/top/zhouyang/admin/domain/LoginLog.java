package top.zhouyang.admin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志对象 eb_sys_login_log
 *
 * @author Zhang Wenxu
 * @date 2023-12-20
 */
@Data
@NoArgsConstructor
@TableName("eb_sys_login_log")
public class LoginLog implements Serializable {
    private static final long serialVersionUID = 1L;

    public LoginLog(String username, Date loginTime, String loginStatus) {
        this.username = username;
        this.loginTime = loginTime;
        this.loginStatus = loginStatus;
    }

    /**
     * Id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private String username;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date loginTime;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 登录状态
     */
    private String loginStatus;
}
