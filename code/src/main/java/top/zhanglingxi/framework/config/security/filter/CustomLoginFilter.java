package top.zhanglingxi.framework.config.security.filter;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Component;
import top.zhanglingxi.admin.domain.LoginLog;
import top.zhanglingxi.admin.domain.User;
import top.zhanglingxi.admin.service.ILoginLogService;
import top.zhanglingxi.admin.service.IUserService;
import top.zhanglingxi.common.exception.LoginTimeLimitException;
import top.zhanglingxi.common.utils.DateUtils;
import top.zhanglingxi.common.utils.RedisUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * 自定义登录过滤器
 * @author Zhang Lingxi
 */
@Component
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${custom.aes.key}")
    private String aesKey;
    @Value("${user.password.maxRetryCount}")
    private Integer retryCount;
    @Value("${user.password.lockTime}")
    private Integer lockTime;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ILoginLogService loginLogService;

    @Autowired
    @Lazy
    private IUserService userService;

    @Override
    @Autowired
    @Lazy
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    @Value("username")
    public void setUsernameParameter(String usernameParameter) {
        super.setUsernameParameter(usernameParameter);
    }

    @Override
    @Value("password")
    public void setPasswordParameter(String passwordParameter) {
        super.setPasswordParameter(passwordParameter);
    }

    @Override
    @Value("/login")
    public void setFilterProcessesUrl(String filterProcessesUrl) {
        super.setFilterProcessesUrl(filterProcessesUrl);
    }

    /**
     * 设置认证成功时使用自定义记住我
     */
    @Override
    @Autowired
    @Lazy
    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        super.setRememberMeServices(rememberMeServices);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try (ServletInputStream is = request.getInputStream()) {
            StringBuilder jsonStr = new StringBuilder();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                jsonStr.append(new String(buf, 0, len));
            }
            String json = jsonStr.toString();
            JSONObject jsonObject = JSONObject.parseObject(json);
            Object uuidObj = jsonObject.get("uuid");
            Object verifyCodeObj = jsonObject.get("verifyCode");
            if (Objects.isNull(uuidObj)) {
                throw new AuthenticationServiceException("uuid不能为空");
            }
            if (Objects.isNull(verifyCodeObj)) {
                throw new AuthenticationServiceException("验证码不能为空");
            }
            String verifyCode = verifyCodeObj.toString();
            String uuid = uuidObj.toString();
            Object cacheObject = redisUtils.getCacheObject("image-code:" + uuid);
            if (Objects.isNull(cacheObject)) {
                throw new AuthenticationServiceException("验证码已过期");
            }
            if (!verifyCode.equals(cacheObject.toString())) {
                throw new AuthenticationServiceException("验证码错误");
            }
            Object usernameObj = jsonObject.get("username");
            Object passwordObj = jsonObject.get("password");
            Object rememberMeObj = jsonObject.get(AbstractRememberMeServices.DEFAULT_PARAMETER);
            if (Objects.isNull(usernameObj)) {
                throw new AuthenticationServiceException("用户名不能为空");
            }
            if (Objects.isNull(passwordObj)) {
                throw new AuthenticationServiceException("密码不能为空");
            }
            if (Objects.nonNull(rememberMeObj)) {
                request.setAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER, rememberMeObj.toString());
            }
            String username = usernameObj.toString();
            String password = passwordObj.toString();
            // 解密密码
            AES aes = SecureUtil.aes(aesKey.getBytes());
            password = aes.decryptStr(password);

            // 判断用户是否超过最大登录失败次数
            User user = userService.selectUserByUsername(username);
            if (Objects.nonNull(user)) {
                Date limitTime = user.getLoginLimitTime();
                // 获取用户被限制登录的时间
                if (Objects.nonNull(limitTime)) {
                    Date nowDate = DateUtils.getNowDate();
                    if (nowDate.before(limitTime)) {
                        // 用户被限制登录
                        long limit = limitTime.getTime() / 1000;
                        long now = nowDate.getTime() / 1000;
                        throw new LoginTimeLimitException(limit - now);
                    }
                } else {
                    // 获取用户登录失败次数
                    Integer loginErrorNum = user.getLoginErrorNum();
                    if (loginErrorNum >= retryCount) {
                        // 限制用户登录时间
                        Date now = DateUtils.getNowDate();
                        Date lockDate = DateUtils.addMinutes(now, lockTime);
                        user.setLoginLimitTime(lockDate);
                        // 清空登录失败次数
                        user.setLoginErrorNum(0);
                        // 更新用户信息
                        userService.updateUser(user);
                        throw new LoginTimeLimitException();
                    } else {
                        // 登录日志
                        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                        Date nowDate = DateUtils.getNowDate();
                        System.out.println("passwordEncoder.encode(password)" + passwordEncoder.encode(password));
                        if (passwordEncoder.matches(password, user.getPassword())) {
                            LoginLog loginLog = new LoginLog(username, nowDate, "成功");
                            loginLogService.insertLoginLog(loginLog);
                        } else {
                            user.setLoginErrorNum(loginErrorNum + 1);
                            // 更新用户信息
                            userService.updateUser(user);
                            LoginLog loginLog = new LoginLog(username, nowDate, "失败");
                            loginLogService.insertLoginLog(loginLog);
                        }
                    }
                }
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authenticationToken);
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("读取请求体中的数据失败");
        }
    }
}
