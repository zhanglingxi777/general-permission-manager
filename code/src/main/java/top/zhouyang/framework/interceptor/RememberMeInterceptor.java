package top.zhouyang.framework.interceptor;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.zhouyang.admin.domain.PersistentLogins;
import top.zhouyang.admin.domain.User;
import top.zhouyang.admin.service.IPersistentLoginsService;
import top.zhouyang.admin.service.IUserService;
import top.zhouyang.common.domain.AjaxResult;
import top.zhouyang.common.utils.DateUtils;
import top.zhouyang.common.utils.RedisUtils;
import top.zhouyang.common.utils.StringUtils;
import top.zhouyang.framework.config.ThreadLocalConfig;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 记住我拦截器
 */
@Component
public class RememberMeInterceptor implements HandlerInterceptor {
    public static final String REMEMBER_ME_COOKIE_NAME = "remember-me";
    @Value("${custom.jwt.key}")
    private String key;
    @Autowired
    private IPersistentLoginsService persistentLoginsService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private ThreadLocalConfig threadLocalConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map map = threadLocalConfig.get();
        if (Objects.nonNull(map)) {
            Object usernameCache = map.get("username");
            if (Objects.nonNull(usernameCache)) {
                // 用户token校验通过且token未失效 无需记住我拦截器进行自动登录 直接放行
                return true;
            } else {
                // 用户token校验通过但token已失效 判断用户是否携带remember-me cookie
                Cookie[] cookies = request.getCookies();
                if (Objects.isNull(cookies) || (cookies.length == 0)) {
                    // 没有携带remember-me cookie，说明未勾选记住我的功能
                    // 用户凭证已过期 记住我拦截器不进行自动登录
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
                    out.write(JSONObject.toJSONString(error));
                    return false;
                } else {
                    Cookie rememberMeCookie = null;
                    // 从request中获取所有token
                    for (Cookie cookie : cookies) {
                        if (REMEMBER_ME_COOKIE_NAME.equals(cookie.getName())) {
                            rememberMeCookie = cookie;
                        }
                    }
                    if (Objects.nonNull(rememberMeCookie)) {
                        // request cookies中携带了remember-me cookie
                        String rememberMeCookieValue = rememberMeCookie.getValue();
                        if (StringUtils.isBlank(rememberMeCookieValue)) {
                            // 如果 rememberMeCookieValue 为空 不进行自动登录
                            response.setCharacterEncoding("UTF-8");
                            PrintWriter out = response.getWriter();
                            AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
                            out.write(JSONObject.toJSONString(error));
                            return false;
                        } else {
                            try {
                                // 自动登录
                                String seriesAndToken = Base64.decodeStr(rememberMeCookieValue);
                                String[] split = seriesAndToken.split(":");
                                String series = split[0];
                                String rememberToken = split[1];
                                LambdaQueryWrapper<PersistentLogins> plQw = new LambdaQueryWrapper<>();
                                plQw.eq(StringUtils.isNotBlank(series), PersistentLogins::getSeries, series);
                                PersistentLogins persistentLogins = persistentLoginsService.getOne(plQw);
                                // 更新token
                                persistentLogins.setToken(UUID.fastUUID().toString());
                                // 更新last used
                                persistentLogins.setLastUsed(DateUtils.getNowDate());
                                persistentLoginsService.updateById(persistentLogins);
                                // 生成token
                                JWTSigner jwtSigner = JWTSignerUtil.hs256(key.getBytes());
                                Date nowDate = DateUtils.getNowDate();
                                Date expireDate = DateUtils.addMinutes(nowDate, 30);
                                String username = persistentLogins.getUsername();
                                String token = JWT.create()
                                        .setExpiresAt(expireDate)
                                        .setPayload("username", username)
                                        .setSigner(jwtSigner)
                                        .sign();
                                // 获取token并存入redis中，以username为key 时效30分钟
                                redisUtils.setCacheObject("login:" + username, token, 30, TimeUnit.MINUTES);
                                // 修改用户最后一次登录时间
                                User user = userService.selectUserByUsername(username);
                                user.setLastLoginTime(DateUtils.getNowDate());
                                userService.updateById(user);

                                // 向浏览器中设置admin token
                                Cookie adminToken = new Cookie("Admin-Token", token);
                                response.addCookie(adminToken);
                                return true;
                            } catch (Exception e) {
                                response.setCharacterEncoding("UTF-8");
                                PrintWriter out = response.getWriter();
                                AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
                                out.write(JSONObject.toJSONString(error));
                                return false;
                            }
                        }
                    } else {
                        // request cookies中没有携带remember-me cookie
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();
                        AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
                        out.write(JSONObject.toJSONString(error));
                        return false;
                    }
                }
            }
        } else {
            // request cookies中没有携带remember-me cookie
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
            out.write(JSONObject.toJSONString(error));
            return false;
        }
    }
}
