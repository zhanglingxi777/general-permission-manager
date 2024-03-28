package top.zhanglingxi.framework.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.zhanglingxi.common.domain.AjaxResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述信息
 *
 * @author Zhang Wenxu
 * @date 2024/03/28
 */
@Aspect
@Component
@Slf4j
public class OperationAspect {
    @Value("${custom.demo}")
    private Boolean demo;

    @Pointcut("execution(public * top.zhanglingxi.admin.controller.*.*(..))")
    public void operationPointcut(){};

    @Around("operationPointcut()")
    public Object aroundOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        if (demo) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String method = request.getMethod();
            log.info("method = {}", method);

            if (!HttpMethod.GET.matches(method)) {
                log.info("当前系统是演示系统，不能进行操作！");
                return AjaxResult.error("当前系统是演示系统，不能进行操作！");
            }
        }
        Object[] args = joinPoint.getArgs();
        return joinPoint.proceed(args);
    }
}
