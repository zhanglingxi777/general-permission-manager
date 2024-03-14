package top.zhanglingxi.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * token验证异常
 */
public class TokenAuthenticationException extends AuthenticationException {

    public TokenAuthenticationException(String msg) {
        super(msg);
    }
}
