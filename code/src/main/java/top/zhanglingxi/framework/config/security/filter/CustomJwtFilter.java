package top.zhanglingxi.framework.config.security.filter;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.zhanglingxi.common.exception.TokenAuthenticationException;
import top.zhanglingxi.common.utils.RedisUtils;
import top.zhanglingxi.common.utils.StringUtils;
import top.zhanglingxi.framework.config.security.custom.CustomAuthenticationFailureHandler;
import top.zhanglingxi.framework.config.security.service.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 自定义JWT过滤器
 * @author Zhang Lingxi
 **/
@Component
@Slf4j
public class CustomJwtFilter extends OncePerRequestFilter {
    @Value("${custom.jwt.key}")
    private String key;
    private final RedisUtils redisUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public CustomJwtFilter(RedisUtils redisUtils, UserDetailsServiceImpl userDetailsService, CustomAuthenticationFailureHandler authenticationFailureHandler) {
        this.redisUtils = redisUtils;
        this.userDetailsService = userDetailsService;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    private static final List<String> WHITE_LIST = Arrays.asList(".*/login", ".*/vcImage", ".*/avatar/.*");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestUri = request.getRequestURI();
            if (!isMatchWhiteList(requestUri)) {
                // 从请求头中获取JWT
                String jwt = null;
                String bearerToken = request.getHeader("Authorization");
                if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                    jwt = bearerToken.substring(7);
                }
                // 验证JWT
                if (Objects.isNull(jwt)) {
                    throw new TokenAuthenticationException("token不存在，请重新登录！");
                }
                if (!JWTUtil.verify(jwt, key.getBytes())) {
                    throw new TokenAuthenticationException("token校验不通过，请重新登录！");
                }
                // 判断token是否过期
                try {
                    JWTValidator.of(jwt).validateDate();
                } catch (ValidateException e) {
                    throw new TokenAuthenticationException("token已过期，请重新登录！");
                }
                // 从JWT中提取用户名
                JWT parsed = JWTUtil.parseToken(jwt);
                Object username = parsed.getPayload("username");
                if (Objects.isNull(username)) {
                    throw new TokenAuthenticationException("token解析失败，请重新登录！");
                }
                // 从redis中获取token
                String token = redisUtils.getCacheObject("login:" + username);
                if (StringUtils.isEmpty(token) || !token.equals(jwt)) {
                    throw new TokenAuthenticationException("token已过期，请重新登录！");
                }
                UserDetails user = userDetailsService.loadUserByUsername(username.toString());
                if (Objects.isNull(user)) {
                    throw new TokenAuthenticationException("token验证失败，请重新登录！");
                }
                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                // 设置请求信息
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将认证对象设置到Security上下文中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenAuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
        } finally {
            // 继续过滤器链
            filterChain.doFilter(request,response);
        }
    }

    /**
     * 请求路径是否匹配白名单
     * @param requestUri 请求路径
     * @return 是否匹配白名单
     */
    private boolean isMatchWhiteList(String requestUri) {
        for (String white : WHITE_LIST) {
            if (requestUri.matches(white)) {
                return true;
            }
        }
        return false;
    }
}
