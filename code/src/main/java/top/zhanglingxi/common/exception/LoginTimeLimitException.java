package top.zhanglingxi.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 用户登录时间被限制
 */
public class LoginTimeLimitException extends AuthenticationException {
    public LoginTimeLimitException() {
        super("用户登录失败超过3次，账号被锁定！");
    }

    public LoginTimeLimitException(Long seconds) {
        super("用户登录受限，请 " + seconds + " 秒后再登录！");
    }
}
