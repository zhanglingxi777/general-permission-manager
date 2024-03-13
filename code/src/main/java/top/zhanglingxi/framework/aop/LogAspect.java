package top.zhanglingxi.framework.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhanglingxi.admin.domain.LoginLog;
import top.zhanglingxi.admin.domain.vo.LoginReqVO;
import top.zhanglingxi.admin.service.ILoginLogService;
import top.zhanglingxi.common.domain.AjaxResult;
import top.zhanglingxi.common.utils.DateUtils;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    private ILoginLogService loginLogService;

    @Pointcut("execution(public * top.zhanglingxi.admin.controller.UserController.login(..))")
    public void loginPointcut(){};

    @Around("loginPointcut()")
    public Object aroundLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (Objects.nonNull(args) && args.length > 0) {
            LoginReqVO reqVO = (LoginReqVO) args[0];
            Object res = joinPoint.proceed(args);
            if (Objects.nonNull(res) && res instanceof AjaxResult) {
                AjaxResult ajaxResult = (AjaxResult) res;
                LoginLog loginLog = new LoginLog();
                loginLog.setUsername(reqVO.getUsername());
                loginLog.setRequestData(JSONObject.toJSONString(reqVO));
                loginLog.setLoginStatus(Integer.parseInt(ajaxResult.get("code").toString()) == 200 ? "成功" : "失败");
                loginLog.setLoginTime(DateUtils.getNowDate());
                log.info("新增登录日志，日志信息 = {}", loginLog);
                loginLogService.save(loginLog);
                return res;
            }
            throw new RuntimeException("请求/login接口，响应数据异常");
        }
        throw new IllegalArgumentException("请求/login接口，请求参数不能为空");
    }
}
