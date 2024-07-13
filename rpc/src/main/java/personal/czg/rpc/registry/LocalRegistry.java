package personal.czg.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *本地注册中心
 */
public class LocalRegistry {
    /**
     * 使用线程安全的concurrentHashMap存储服务信息
     */
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     */
    public static void register(String serviceName, Class<?> imlClass){
        map.put(serviceName, imlClass);
    }

    /**
     * 获取服务
     */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /**
     * 删除服务
     */

    public static void remove(String serviceName){
        map.remove(serviceName);
    }

}

