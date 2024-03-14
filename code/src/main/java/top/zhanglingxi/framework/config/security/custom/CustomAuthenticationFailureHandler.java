package top.zhanglingxi.framework.config.security.custom;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import top.zhanglingxi.common.constant.HttpStatus;
import top.zhanglingxi.common.domain.AjaxResult;
import top.zhanglingxi.common.exception.LoginTimeLimitException;
import top.zhanglingxi.common.exception.TokenAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 自定义登录校验失败处理
 * @author Zhang Lingxi
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        AjaxResult error = null;
        String message;
        if (exception instanceof AccountExpiredException) {
            message = "账户过期，登录失败！";
        } else if (exception instanceof LoginTimeLimitException) {
            message = exception.getMessage();
            error = AjaxResult.error(HttpStatus.ERROR, message);
        } else if (exception instanceof BadCredentialsException) {
            message = "用户名或密码错误，登录失败！";
        } else if (exception instanceof CredentialsExpiredException) {
            message = "密码过期，登录失败！";
        } else if (exception instanceof DisabledException) {
            message = "账户被禁用，登录失败！";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            message = "账户不存在，登录失败！";
        } else if (exception instanceof AuthenticationServiceException) {
            message = exception.getMessage();
        } else if (exception instanceof TokenAuthenticationException) {
            // token验证失败，跳转到登录页面
            message = exception.getMessage();
            error = AjaxResult.error(HttpStatus.UNAUTHORIZED, message);
        } else {
            message = "登录失败！";
        }
        if (Objects.isNull(error)) {
            error = AjaxResult.error(HttpStatus.ERROR, message);
        }
        OutputStream out = response.getOutputStream();
        out.write(JSONObject.toJSONBytes(error));
        out.flush();
        out.close();
    }
}
