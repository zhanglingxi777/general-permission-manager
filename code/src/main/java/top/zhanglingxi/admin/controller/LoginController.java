package top.zhanglingxi.admin.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhanglingxi.admin.domain.User;
import top.zhanglingxi.admin.domain.vo.LoginUserInfoVO;
import top.zhanglingxi.admin.domain.vo.TokenVO;
import top.zhanglingxi.admin.service.IUserService;
import top.zhanglingxi.common.domain.AjaxResult;
import top.zhanglingxi.common.utils.DateUtils;
import top.zhanglingxi.common.utils.RedisUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 登录相关请求 controller
 *
 * @author Zhang Wenxu
 * @date 2024/03/15
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Value("${custom.jwt.key}")
    private String key;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IUserService userService;

    /**
     * 获取验证码
     *
     * @return 验证码
     */
    @GetMapping("/vcImage")
    public AjaxResult image() {
        UUID uuid = UUID.randomUUID();
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 80, 4, 10);
        captcha.setGenerator(new RandomGenerator("0123456789", 4));
        captcha.createCode();
        String code = captcha.getCode();
        redisUtils.setCacheObject("image-code:" + uuid, code, 5, TimeUnit.MINUTES);
        Map<String, Object> data = new HashMap<>();
        data.put("uuid", uuid);
        data.put("image", captcha.getImageBase64Data());
        return AjaxResult.success(data);
    }

    /**
     * 刷新token
     *
     * @return 新的token
     */
    @GetMapping("/refreshToken")
    public AjaxResult refreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();
        String username = loginUser.getUsername();
        JWTSigner jwtSigner = JWTSignerUtil.hs256(key.getBytes());
        Date nowDate = DateUtils.getNowDate();
        Date expireDate = DateUtils.addMinutes(nowDate, 30);
        String token = JWT.create()
                .setExpiresAt(expireDate)
                .setPayload("username", username)
                .setSigner(jwtSigner)
                .sign();
        redisUtils.deleteObject("login:" + username);
        redisUtils.setCacheObject("login:" + username, token, 30, TimeUnit.MINUTES);
        TokenVO tokenVO = new TokenVO(expireDate.getTime(), token);
        return AjaxResult.success("token刷新成功", tokenVO);
    }

    /**
     * 强制注销登录
     *
     * @param username 用户名
     * @return 是否成功
     */
    @GetMapping("/force/{username}")
    public AjaxResult forceLogout(@PathVariable String username) {
        if ("admin".equals(username)) {
            return AjaxResult.error("不允许注销超级管理员！");
        }
        if (redisUtils.deleteObject("login:" + username)) {
            return AjaxResult.success("注销成功");
        }
        return AjaxResult.error("注销失败！");
    }

    /**
     * 获取登录用户信息列表
     *
     * @return 登录用户信息列表
     */
    @GetMapping("/user")
    public AjaxResult loginUser() {
        Collection<String> keys = redisUtils.keys("login:" + "*");
        List<LoginUserInfoVO> loginUserList = new ArrayList<>();
        keys.forEach(key -> {
            String[] split = key.split(":");
            int length = split.length;
            String username = split[length - 1];
            User user = userService.selectUserByUsername(username);
            Date lastLoginTime = user.getLastLoginTime();
            LoginUserInfoVO loginUser = new LoginUserInfoVO(username, lastLoginTime);
            loginUserList.add(loginUser);
        });
        return AjaxResult.success(loginUserList);
    }
}
