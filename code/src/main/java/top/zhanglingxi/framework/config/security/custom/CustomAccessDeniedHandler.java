package top.zhanglingxi.framework.config.security.custom;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
 * 认证用户无权限访问资源处理器
 * code: 403
 * @author Zhang Lingxi
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        String message = accessDeniedException.getMessage();
        PrintWriter out = response.getWriter();
        AjaxResult error = AjaxResult.error(HttpStatus.FORBIDDEN, message);
        out.write(new String(JSONObject.toJSONBytes(error), StandardCharsets.UTF_8));
        out.close();
    }
}
