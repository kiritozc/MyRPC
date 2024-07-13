package personal.czg.rpc.starter.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import personal.czg.rpc.RpcApplication;
import personal.czg.rpc.config.RpcConfig;
import personal.czg.rpc.fault.tolerantStrategy.WatchFault;
import personal.czg.rpc.server.HttpServer;
import personal.czg.rpc.server.VertexHttpServer;
import personal.czg.rpc.starter.annotation.EnableRpc;

import java.io.IOException;

/**
 * 在SpringBoot初始化时，让Rpc 框架启动
 */
@Slf4j
public class EnableBootStrap implements ImportBeanDefinitionRegistrar {

    /**
     * Spring 初始化时执行，初始化 RPC 框架
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        //getAnnotationAttributes能获得类上面的注解
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");

        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();

        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 启动服务器
        if (needServer) {
            // 启动web
            HttpServer httpServer = new VertexHttpServer();
            httpServer.serverStart(RpcApplication.getRpcConfig().getPort());

        } else {
            log.info("不启动 server");
        }

    }
}