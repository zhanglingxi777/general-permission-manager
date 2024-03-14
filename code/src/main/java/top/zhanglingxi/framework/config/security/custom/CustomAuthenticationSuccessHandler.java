package top.zhanglingxi.framework.config.security.custom;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.zhanglingxi.admin.domain.User;
import top.zhanglingxi.admin.service.IUserService;
import top.zhanglingxi.common.domain.AjaxResult;
import top.zhanglingxi.common.utils.DateUtils;
import top.zhanglingxi.common.utils.RedisUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 自定义登录校验成功处理
 * @author Zhang Lingxi
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${custom.jwt.key}")
    private String key;
    private final RedisUtils redisUtils;
    @Lazy
    private final IUserService userService;

    @Autowired
    public CustomAuthenticationSuccessHandler(RedisUtils redisUtils, IUserService userService) {
        this.redisUtils = redisUtils;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8;");
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        // 生成jwt
        JWTSigner jwtSigner = JWTSignerUtil.hs256(key.getBytes());
        Date nowDate = DateUtils.getNowDate();
        Date expireDate = DateUtils.addMinutes(nowDate, 30);
        String token = JWT.create()
                .setExpiresAt(expireDate)
                .setPayload("username", username)
                .setSigner(jwtSigner)
                .sign();
        // 获取token并存入redis中，以username为key
        redisUtils.setCacheObject("login:" + username, token, 30, TimeUnit.MINUTES);
        // 修改用户最后一次登录时间
        User loginUser = userService.selectUserByUsername(username);
        loginUser.setLastLoginTime(DateUtils.getNowDate());
        userService.updateUser(loginUser);
        // 返回数据
        AjaxResult ajax = AjaxResult.success("登录成功", user);
        ajax.put("token", token);
        ajax.put("expireTime", expireDate.getTime());
        OutputStream out = response.getOutputStream();
        out.write(JSONObject.toJSONBytes(ajax));
        out.flush();
        out.close();
    }
}
