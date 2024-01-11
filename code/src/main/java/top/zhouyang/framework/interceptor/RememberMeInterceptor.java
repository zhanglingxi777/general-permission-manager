package top.zhouyang.framework.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.zhouyang.admin.service.IPersistentLoginsService;
import top.zhouyang.common.utils.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 记住我拦截器
 */
@Component
public class RememberMeInterceptor implements HandlerInterceptor {
    public static final String REMEMBER_ME_COOKIE_NAME = "remember-me";
    @Autowired
    private IPersistentLoginsService persistentLoginsService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies) || (cookies.length == 0)) {
            return true;
        }
        for (Cookie cookie : cookies) {
            if (REMEMBER_ME_COOKIE_NAME.equals(cookie.getName())) {
                String rememberMeCookie = cookie.getValue();
                if (StringUtils.isBlank(rememberMeCookie)) {
                    return true;
                }
                // 自动登录

            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
