package top.zhouyang.admin.domain.vo.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 重置密码query VO类
 */
@Data
public class ResetPwdQueryVO implements Serializable {
    public static final Long SerialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 原密码
     */
    private String oldPwd;

    /**
     * 新密码
     */
    private String newPwd;

    /**
     * 确认密码
     */
    private String confirmPwd;
}
