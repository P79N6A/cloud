package com.springframework.common.support.controller.aop;

import com.springframework.common.core.ResultCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * controller控制层AOP 对异常处理,日志记录
 *
 * @author summer
 */
@Component
public class ControllerAop {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAop.class);

    @Around("execution(* com.*.*.*ResultCode *(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        long startTime = Clock.systemDefaultZone().millis();
        ResultCode<?> result;
        try {
            result = (ResultCode<?>) pjp.proceed();
            logger.info(pjp.getSignature() + "use time:" + (Clock.systemDefaultZone().millis() - startTime));
        } catch (Throwable e) {
            result = handlerException(pjp, e);
        } finally {
            after();
        }
        return result;
    }

    private void after() {

    }

    private ResultCode<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResultCode<?> result;
        // 已知异常
        if (e instanceof Exception) {
            result = ResultCode.getFailure(ResultCode.CODE_KNOWN_ERROR, e.getLocalizedMessage());
        } else {
            logger.error(pjp.getSignature() + " error ", e);
            result = ResultCode.getFailure(ResultCode.CODE_UNKNOWN_ERROR, e.getLocalizedMessage());
            // 未知异常是应该重点关注的，这里可以做其他操作，如通知邮件，单独写到某个文件等等。
        }
        return result;
    }
}
