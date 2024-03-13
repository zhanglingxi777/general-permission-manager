package top.zhanglingxi.framework.interceptor;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.zhanglingxi.common.domain.AjaxResult;
import top.zhanglingxi.common.utils.RedisUtils;
import top.zhanglingxi.common.utils.StringUtils;
import top.zhanglingxi.framework.config.ThreadLocalConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * token 拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Value("${custom.jwt.key}")
    private String key;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ThreadLocalConfig threadLocalConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean pass = true;
        String message = "请重新登录!";
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) {
            // 未携带token 拦截
            message = "请求未携带凭证,请重新登录!";
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), message);
            out.write(JSONObject.toJSONString(error));
            return false;
        } else {
            // 携带了token 校验token
            if (authorization.startsWith("Bearer ")) {
                String[] split = authorization.split(" ");
                String token = split[1];
                // 校验token
                if (JWTUtil.verify(token, key.getBytes())) {
                    try {
                        // 判断token是否过期
                        JWTValidator.of(token).validateDate();
                        // 从JWT中提取用户名
                        JWT parsed = JWTUtil.parseToken(token);
                        Object username = parsed.getPayload("username");
                        if (Objects.nonNull(username)) {
                            // 从redis中获取token
                            String tokenCache = redisUtils.getCacheObject("login:" + username);
                            if (StringUtils.isNoneBlank(token) && tokenCache.equals(token)) {
                                // 所有校验都通过 放行
                                // 将用户名 token等信息放入threadLocal中
                                Map<String, String> map = new HashMap<>();
                                map.put("username", username.toString());
                                map.put("token", token);
                                threadLocalConfig.set(map);
                                return pass;
                            } else {
                                // 用户凭证已过期 交给rememberMe拦截器
                                return true;
                            }
                        } else {
                            message = "用户凭证校验失败,请重新登录!";
                            response.setCharacterEncoding("UTF-8");
                            PrintWriter out = response.getWriter();
                            AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), message);
                            out.write(JSONObject.toJSONString(error));
                            return false;
                        }
                    } catch (ValidateException e) {
                        // 用户凭证已过期 交给rememberMe拦截器
                        return true;
                    }
                } else {
                    // token校验失败
                    message = "用户凭证校验失败,请重新登录!";
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), message);
                    out.write(JSONObject.toJSONString(error));
                    return false;
                }
            } else {
                // token 格式错误
                message = "用户凭证校验失败,请重新登录!";
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), message);
                out.write(JSONObject.toJSONString(error));
                return false;
            }
        }
    }
}