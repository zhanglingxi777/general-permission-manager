package top.zhanglingxi.framework.config.security.custom;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.zhanglingxi.common.constant.HttpStatus;
import top.zhanglingxi.common.domain.AjaxResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 匿名用户无权限访问处理器
 * code: 401
 * @author Zhang Lingxi
 */
@Component
public class AnonymousAuthenticationHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED, "请先登录！");
        PrintWriter out = response.getWriter();
        out.write(new String(JSONObject.toJSONBytes(error), StandardCharsets.UTF_8));
        out.close();
    }
}
