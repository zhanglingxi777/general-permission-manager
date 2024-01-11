package top.zhouyang.admin.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class LoginReqVO implements Serializable {
    public static final Long SerialVersionUID = 1L;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空!")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空!")
    private String password;

    /**
     * uuid
     */
    @NotBlank(message = "uuid不能为空!")
    private String uuid;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空!")
    private String verifyCode;

    /**
     * 记住我
     */
    @NotNull(message = "记住我不能为空!")
    private Boolean rememberMe;
}
