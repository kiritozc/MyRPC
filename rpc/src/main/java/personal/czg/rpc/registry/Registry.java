package personal.czg.rpc.registry;

import personal.czg.rpc.config.RegistryConfig;
import personal.czg.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心功能接口
 */
public interface Registry {
    /**
     * 初始化
     */
    void init(RegistryConfig registryConfig);

    /**
     * 服务注册
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务注销
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 报告服务错误
     */
    void put(String address, String serviceName);
}
