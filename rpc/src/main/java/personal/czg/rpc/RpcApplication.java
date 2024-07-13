package personal.czg.rpc;


import lombok.extern.slf4j.Slf4j;
import personal.czg.rpc.config.RegistryConfig;
import personal.czg.rpc.config.RpcConfig;
import personal.czg.rpc.constant.RpcConstant;
import personal.czg.rpc.registry.Registry;
import personal.czg.rpc.registry.RegistryFactory;
import personal.czg.rpc.utils.ConfigUtil;

/**
 * 获取RPC框架的整体配置
 * ------------------------------
 * getRpcConfig 采用   双检锁单例模式  实现
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * 通过一个指定的配置类初始化RPC框架的配置
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc配置: {}", newRpcConfig.toString());

        //初始化注册中心
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("注册中心配置：{}", registryConfig);
    }

    /**
     * 通过读取配置文件初始化RPC框架
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try{
            newRpcConfig = ConfigUtil.loadConfig(RpcConfig.class, RpcConstant.RPC_PREFIX);
        } catch (Exception e){
            newRpcConfig = new RpcConfig();
            log.error("读取配置文件出错");
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置类
     * 采用   -----双检锁单例模式 ----- 实现
     */
    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized(RpcApplication.class){
                if(rpcConfig == null){
                    init();
                    return rpcConfig;
                }
            }
        }
        return rpcConfig;
    }

}
