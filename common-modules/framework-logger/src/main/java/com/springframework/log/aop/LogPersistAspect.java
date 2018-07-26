package com.springframework.log.aop;

import com.alibaba.fastjson.JSON;
import com.springframework.log.domain.InterfaceLog;
import com.springframework.log.monitor.SoaResponse;
import com.springframework.log.service.InterfaceLogService;
import com.springframework.log.util.ThreadLocalUtil;
//import com.weimob.soa.common.response.SoaResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
public class LogPersistAspect {

    @Autowired
    private InterfaceLogService interfaceLogService;

    @AfterReturning(value = "@annotation(com.springframework.log.annotation.LogPersist)", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String isDubboCall = ThreadLocalUtil.get("isDubboCall", String.class);
        if(StringUtils.isEmpty(isDubboCall)){
            return;
        }
        if (!(result instanceof SoaResponse)) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> intefaceClass = joinPoint.getTarget().getClass();
        Object[] args = joinPoint.getArgs();
        String consumerApplication = ThreadLocalUtil.get("consumerApplication", String.class);
        String consumerHost = ThreadLocalUtil.get("consumerHost", String.class);
        String providerApplication = ThreadLocalUtil.get("providerApplication", String.class);
        String providerHost = ThreadLocalUtil.get("providerHost", String.class);
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setConsumerApplication(consumerApplication);
        interfaceLog.setConsumerHost(consumerHost);
        interfaceLog.setProviderApplication(providerApplication);
        interfaceLog.setProviderHost(providerHost);
        interfaceLog.setInterfaceName(intefaceClass.getName());
        interfaceLog.setMethodeName(method.getName());
        interfaceLog.setRequestParam(args == null ? null : JSON.toJSONString(args));
        interfaceLog.setResponse(JSON.toJSONString(result));


        SoaResponse soaResponse = (SoaResponse) result;
        if ("0".equals(soaResponse.getReturnCode()) || "000000".equals(soaResponse.getReturnCode())) {
            interfaceLogService.insertSuccessLog(interfaceLog);
        } else {
            interfaceLogService.insertErrorLog(interfaceLog);
        }
    }
}
