package top.zhanglingxi.framework.config.security.custom;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import top.zhanglingxi.common.domain.AjaxResult;
import top.zhanglingxi.common.utils.RedisUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义登录成功处理类
 * @author Zhang Lingxi
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RedisUtils redisUtils;

    @Autowired
    public CustomLogoutSuccessHandler(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=utf-8");
        UserDetails user = (UserDetails)authentication.getPrincipal();
        // 清除 redis中的缓存
        redisUtils.deleteObject("login:" + user.getUsername());
        AjaxResult success = AjaxResult.success("注销成功");
        out.write(JSONObject.toJSONString(success));
    }
}
