package personal.czg.rpc.proxy;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ServiceProxyFactory {
    /**
     * 根据服务类获取服务类的代理对象
     */
    public static <T> T getProxy(Class<T> serviceCLass){
        return (T) Proxy.newProxyInstance(
                serviceCLass.getClassLoader(),
                new Class[]{serviceCLass},
                new ServiceProxy()
        );
    }
}
