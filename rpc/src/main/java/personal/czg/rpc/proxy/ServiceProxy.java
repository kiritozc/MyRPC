package personal.czg.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import personal.czg.rpc.RpcApplication;
import personal.czg.rpc.config.RpcConfig;
import personal.czg.rpc.constant.RetryConfig;
import personal.czg.rpc.constant.RpcConstant;
import personal.czg.rpc.fault.Retry;
import personal.czg.rpc.fault.RetryFactory;
import personal.czg.rpc.model.RpcRequest;
import personal.czg.rpc.model.RpcResponse;
import personal.czg.rpc.model.ServiceMetaInfo;
import personal.czg.rpc.registry.Registry;
import personal.czg.rpc.registry.RegistryFactory;
import personal.czg.rpc.serializer.JdkSerializer;
import personal.czg.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = new JdkSerializer();

        //帮消费端构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .args(args)
                .parameterTypes(method.getParameterTypes())
                .build();

        try{
            //序列化
            byte[] bytes = serializer.serialize(rpcRequest);

            //从注册中心获取服务地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            //发送请求
            String targetAddress = selectedServiceMetaInfo.getServiceAddress();

            //重试策略
            Retry retry = RetryFactory.getInstance(RetryConfig.FIXED_INTERVAL);
            try{
                HttpResponse httpResponse = retry.doRetry(() -> HttpRequest.post(targetAddress).body(bytes).execute());
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                System.out.println(rpcResponse.getMessage());
                return rpcResponse.getData();
            } catch (Exception e){
                System.out.println("远程服务获取失败，请稍后再试");
                //给注册中心报告错误
                String wrongServiceKey = "wrong:"+selectedServiceMetaInfo.getServiceHost();
                String wrongServiceInfo = JSONUtil.toJsonStr(serviceMetaInfo);
                registry.put(wrongServiceKey, wrongServiceInfo);
                return method.getReturnType().newInstance();
            }

//            try(HttpResponse httpResponse = HttpRequest.post(targetAddress)
//                    .body(bytes)
//                    .execute()){
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                System.out.println(rpcResponse.getMessage());
//                return rpcResponse.getData();
//            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
