package personal.czg.rpc.starter.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import personal.czg.rpc.RpcApplication;
import personal.czg.rpc.config.RegistryConfig;
import personal.czg.rpc.config.RpcConfig;
import personal.czg.rpc.model.ServiceMetaInfo;
import personal.czg.rpc.registry.LocalRegistry;
import personal.czg.rpc.registry.Registry;
import personal.czg.rpc.registry.RegistryFactory;
import personal.czg.rpc.starter.annotation.RpcService;

@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行，注册服务
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        //获取Bean的注解
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        //如果注解为空则不注册
        if (rpcService != null) {
            // 需要注册服务
            // 1. 获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            // 默认值处理
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();
            // 2. 注册服务
            // 本地注册
            LocalRegistry.register(serviceName, beanClass);

            // 全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServeHost());
            serviceMetaInfo.setPort(rpcConfig.getPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}