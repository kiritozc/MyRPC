package personal.czg.rpc.config;

import lombok.Data;

@Data
public class RegistryConfig {
    /**
     * 注册中心类别，即技术选型
     */
    private String registry = "etcd";

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 尝试超时时间：ms
     */
    private Long timeout = 10000L;
}
