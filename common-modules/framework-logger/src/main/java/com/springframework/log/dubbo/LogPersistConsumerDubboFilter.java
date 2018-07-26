//package com.weimob.cube.log.dubbo;
//
//
//import com.alibaba.dubbo.common.Constants;
//import com.alibaba.dubbo.common.URL;
//import com.alibaba.dubbo.common.extension.Activate;
//import com.alibaba.dubbo.common.proxy.ProxyRpcInvocation;
//import com.alibaba.dubbo.common.utils.StringUtils;
//import com.alibaba.dubbo.rpc.*;
//import com.alibaba.fastjson.JSON;
//import LogConfigProperties;
//import InterfaceLog;
//import InterfaceLogService;
//import InterfaceLogServiceImpl;
//import ApplicationContextUtil;
//import com.weimob.soa.common.response.SoaResponse;
//
//import java.util.Set;
//
//@Activate(group = Constants.CONSUMER, order = 99)
//public class LogPersistConsumerDubboFilter implements Filter {
//    private static final Set<String> logProperties = LogConfigProperties.getLogProperties();
//    private InterfaceLogService interfaceLogService;
//
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        if (interfaceLogService == null) {
//            interfaceLogService = ApplicationContextUtil.getBean(InterfaceLogServiceImpl.class.getName(), InterfaceLogService.class);
//        }
//        Result result = invoker.invoke(invocation);
//        Object response = result.getValue();
//        if (!(response instanceof SoaResponse)) {
//            return result;
//        }
//        String interfaceName = invoker.getInterface().getName();
//        String methodName = invocation.getMethodName();
//        //是否通过代理
//        if (ProxyRpcInvocation.INTERFACE_NAME.equals(interfaceName) && methodName.equals(ProxyRpcInvocation.METHOD_NAME)
//                && invocation.getArguments().length == 1
//                && invocation.getArguments()[0] instanceof ProxyRpcInvocation) {
//            ProxyRpcInvocation proxyRpcInvocation = (ProxyRpcInvocation) invocation.getArguments()[0];
//            interfaceName = proxyRpcInvocation.getServiceName(); // 获取原始接口名称
//            methodName = proxyRpcInvocation.getMethodName(); // 获取原始方法名
//        }
//
//        if (!logProperties.contains(LogConfigProperties.format(interfaceName, methodName))) {
//            return result;
//        }
//
//        URL url = invoker.getUrl();
//        RpcContext rpcContext = RpcContext.getContext(); // 提供方必须在invoke()之前获取context信息
//        String consumerApplication = "";
//        String consumerHost = "";
//        if (StringUtils.isBlank(rpcContext.getProxySrcApplicationName())) {
//            consumerApplication = url.getParameter(Constants.APPLICATION_KEY); //当前应用名称
//            consumerHost = rpcContext.getLocalHost();
//        } else {//通过soa-proxy代理调用获取原始消费端应用名称、ip地址
//            consumerApplication = rpcContext.getProxySrcApplicationName();
//            consumerHost = rpcContext.getProxySrcApplicationIp();
//        }
//        String providerApplication = url.getParameter(Constants.PROVIDER_PROTOCOL);
//        String providerHost = url.getHost();
//        if (StringUtils.isEmpty(providerHost)) {
//            providerHost = RpcContext.getContext().getRemoteHost() + ":" + RpcContext.getContext().getRemotePort();
//        }
//
//        Object[] args = invocation.getArguments();
//        InterfaceLog interfaceLog = new InterfaceLog();
//        interfaceLog.setConsumerApplication(consumerApplication);
//        interfaceLog.setConsumerHost(consumerHost);
//        interfaceLog.setProviderApplication(providerApplication);
//        interfaceLog.setProviderHost(providerHost);
//        interfaceLog.setInterfaceName(interfaceName);
//        interfaceLog.setMethodeName(methodName);
//        interfaceLog.setRequestParam(args == null ? null : JSON.toJSONString(args));
//        interfaceLog.setResponse(JSON.toJSONString(response));
//
//        SoaResponse soaResponse = (SoaResponse) response;
//        if ("0".equals(soaResponse.getReturnCode()) || "000000".equals(soaResponse.getReturnCode())) {
//            interfaceLogService.insertSuccessLog(interfaceLog);
//        } else {
//            interfaceLogService.insertErrorLog(interfaceLog);
//        }
//        return result;
//    }
//}
