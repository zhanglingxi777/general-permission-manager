package top.zhouyang.framework.interceptor;

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
import top.zhouyang.common.domain.AjaxResult;
import top.zhouyang.common.utils.RedisUtils;
import top.zhouyang.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * token 拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Value("${custom.jwt.key}")
    private String key;
    @Autowired
    private RedisUtils redisUtils;
    public static final List<String> WITHE_LIST = Arrays.asList("/sys/user/vcImage", "/sys/user/login");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        System.out.println("uri: " + uri);
        // 在白名单中的请求直接放行
        if (WITHE_LIST.contains(uri)){
            return true;
        } else {
            PrintWriter out = response.getWriter();
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isBlank(authorization)) {
                // 未携带token 拦截
                AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "请求未携带凭证,请重新登录!");
                out.write(JSONObject.toJSONString(error));
                return false;
            } else {
                // 携带了token 校验token
                if (!authorization.startsWith("Bearer ")) {
                    // token 格式错误
                    AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证校验失败,请重新登录!");
                    out.write(JSONObject.toJSONString(error));
                    return false;
                }
                String[] split = authorization.split(" ");
                String token = split[1];
                // 校验token
                if (!JWTUtil.verify(token, key.getBytes())) {
                    // token校验失败
                    AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证校验失败,请重新登录!");
                    out.write(JSONObject.toJSONString(error));
                    return false;
                }
                // 判断token是否过期
                try {
                    JWTValidator.of(token).validateDate();
                } catch (ValidateException e) {
                    AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录!");
                    out.write(JSONObject.toJSONString(error));
                    return false;
                }
                // 从JWT中提取用户名
                JWT parsed = JWTUtil.parseToken(token);
                Object username = parsed.getPayload("username");
                if (Objects.isNull(username)) {
                    AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证校验失败,请重新登录!");
                    out.write(JSONObject.toJSONString(error));
                    return false;
                }
                // 从redis中获取token
                String tokenCache = redisUtils.getCacheObject("login:" + username);
                if (StringUtils.isEmpty(token) || !tokenCache.equals(token)) {
                    AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录!");
                    out.write(JSONObject.toJSONString(error));
                    return false;
                }
                // 所有校验都通过 放行
                // 将用户名放入threadLocal中
                ThreadLocal<String> threadLocal = new ThreadLocal<>();
                threadLocal.set(username.toString());
                return true;
            }
        }
    }
}
