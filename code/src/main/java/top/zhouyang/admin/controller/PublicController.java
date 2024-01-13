package top.zhouyang.admin.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/public")
public class PublicController {
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

    @PostMapping("/auto/login")
    public AjaxResult autoLogin(HttpServletRequest request) {
        // 用户token校验通过但token已失效 判断用户是否携带remember-me cookie
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies) || (cookies.length == 0)) {
            // 没有携带remember-me cookie，说明未勾选记住我的功能
            // 用户凭证已过期 记住我拦截器不进行自动登录
            return AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
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
                    return AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
                } else {
                    try {
                        // 自动登录
                        String seriesAndToken = Base64.decodeStr(rememberMeCookieValue);
                        String[] split = seriesAndToken.split(":");
                        String series = split[0];
                        String rememberToken = split[1];
                        LambdaQueryWrapper<PersistentLogins> plQw = new LambdaQueryWrapper<>();
                        // 序列号
                        plQw.eq(StringUtils.isNotBlank(series), PersistentLogins::getSeries, series);
                        PersistentLogins persistentLogins = persistentLoginsService.getOne(plQw);
                        // 更新token
                        persistentLogins.setToken(UUID.fastUUID().toString());
                        // 更新last used
                        persistentLogins.setLastUsed(DateUtils.getNowDate());
                        persistentLoginsService.update(persistentLogins, plQw);
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
                        AjaxResult success = AjaxResult.success("自动登录成功！");
                        success.put("token", token);
                        return success;
                    } catch (Exception e) {
                        return AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
                    }
                }
            } else {
                // request cookies中没有携带remember-me cookie
                return AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "用户凭证已过期,请重新登录！");
            }
        }
    }
}
