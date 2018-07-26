//package com.weimob.cube.log.dubbo;
//
//
//import com.alibaba.dubbo.common.Constants;
//import com.alibaba.dubbo.common.URL;
//import com.alibaba.dubbo.common.extension.Activate;
//
//import com.alibaba.dubbo.common.utils.StringUtils;
//import com.alibaba.dubbo.rpc.*;
//import com.alibaba.fastjson.JSON;
//
//import InterfaceLogService;
//import InterfaceLogServiceImpl;
//import ApplicationContextUtil;
//import ThreadLocalUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import java.lang.reflect.Method;
//import java.util.Map;
//
//@Activate(group = Constants.PROVIDER, order = 99)
//@Slf4j
//public class LogPersistProviderDubboFilter implements Filter {
//
//    private InterfaceLogService interfaceLogService;
//
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        if (interfaceLogService == null) {
//            interfaceLogService = ApplicationContextUtil.getBean(InterfaceLogServiceImpl.class.getName(), InterfaceLogService.class);
//        }
//        URL url = invoker.getUrl();
//        RpcContext rpcContext = RpcContext.getContext(); // 提供方必须在invoke()之前获取context信息
//        String consumerApplication = "";
//        String consumerHost = "";
//        if (StringUtils.isBlank(rpcContext.getProxySrcApplicationName())) {
//            consumerApplication = rpcContext.getAttachment(Constants.DUBBO_PROXY_CONSUMER_APPLICATION_NAME); //当前应用名称
//            consumerHost = rpcContext.getRemoteHost();
//        } else {//通过soa-proxy代理调用获取原始消费端应用名称、ip地址
//            consumerApplication = rpcContext.getProxySrcApplicationName();
//            consumerHost = rpcContext.getProxySrcApplicationIp();
//        }
//
//        log.info("url.getParameters:{}", JSON.toJSONString(url.getParameters()));
//        log.info("rpcContext.getArguments:{}", JSON.toJSONString(rpcContext.getAttachments()));
//
//        String providerApplication = url.getParameter(Constants.APPLICATION_KEY);
//        String providerHost = rpcContext.getLocalHost();
//        if (StringUtils.isEmpty(providerHost)) {
//            providerHost = rpcContext.getRemoteHost() + ":" + RpcContext.getContext().getRemotePort();
//        }
//        ThreadLocalUtil.set("consumerApplication", consumerApplication);
//        ThreadLocalUtil.set("consumerHost", consumerHost);
//        ThreadLocalUtil.set("providerApplication", providerApplication);
//        ThreadLocalUtil.set("providerHost", providerHost);
//        ThreadLocalUtil.set("isDubboCall", "true");
//
//        Result result = invoker.invoke(invocation);
//        return result;
//    }
//}
