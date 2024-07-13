package personal.czg.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import personal.czg.rpc.config.RpcConfig;

public class ConfigUtil {


    /**
     * 读取名为application-env.properties的配置文件，并返回一个配置类对象
     * @param tClass
     * @param prefix
     * @param env
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String env){
        StringBuilder configFileBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(env)){
            configFileBuilder.append("-").append(env);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());

        return props.toBean(tClass, prefix);
    }

    /**
     * 读取无env参数的配置文件
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix){
        return loadConfig(tClass, prefix, "");
    }


}
