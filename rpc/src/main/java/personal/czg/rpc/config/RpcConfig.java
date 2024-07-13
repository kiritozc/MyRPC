package personal.czg.rpc.config;

import lombok.Data;
import personal.czg.rpc.constant.RetryConfig;

/**
 * 存储所有配置项
 * 赋予值均为默认值，后续根据配置文件设置
 */

@Data
public class RpcConfig {
    /**
     * 标记属性
     */
    private String name = "c-RPC";
    private String version = "2.0";

    /**
     * 网络属性
     */
    private String serveHost = "localhost";
    private Integer port = 8086;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 重试机制
     */
    private String retryStrategy = RetryConfig.FIXED_INTERVAL;
}
