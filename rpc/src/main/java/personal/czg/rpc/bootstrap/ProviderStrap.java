package personal.czg.rpc.bootstrap;

import personal.czg.rpc.RpcApplication;
import personal.czg.rpc.config.RegistryConfig;
import personal.czg.rpc.config.RpcConfig;
import personal.czg.rpc.constant.RpcConstant;
import personal.czg.rpc.model.ServiceMetaInfo;
import personal.czg.rpc.model.ServiceRegisterInfo;
import personal.czg.rpc.registry.LocalRegistry;
import personal.czg.rpc.registry.Registry;
import personal.czg.rpc.registry.RegistryFactory;
import personal.czg.rpc.server.HttpServer;
import personal.czg.rpc.server.VertexHttpServer;

import java.util.List;

public class ProviderStrap {
    public static void init(List<ServiceRegisterInfo<?>> serviceImplList){
        // 初始化 RPC名、版本、地址、端口-注册中心地址和端口-服务者地址和端口
        RpcApplication.init();

        // 注册到注册中心
        // 获取rpc配置对象
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        // 从rpc配置对象  中取出  注册中心配置对象
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        // 利用 注册中心配置对象 里存的 注册中心的信息(名字)  从注册中心工厂  获取 注册中心对象
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        for(ServiceRegisterInfo<?> info:serviceImplList){
            //本地注册
            String serviceName = info.getServiceName();
            LocalRegistry.register(serviceName,info.getImplClass());

            // 注册服务到注册中心
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            serviceMetaInfo.setServiceHost(rpcConfig.getServeHost());
            serviceMetaInfo.setPort(rpcConfig.getPort());

            try{
                registry.register(serviceMetaInfo);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }

        // 启动web
        HttpServer httpServer = new VertexHttpServer();
        httpServer.serverStart(RpcApplication.getRpcConfig().getPort());
    }
}
