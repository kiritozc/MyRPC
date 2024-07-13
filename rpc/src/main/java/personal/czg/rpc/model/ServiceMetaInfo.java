package personal.czg.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 注册服务的信息
 */
@Data
public class ServiceMetaInfo {
    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务地址
     */
    private String serviceHost;

    /**
     * 端口
     */
    private Integer port;

    /**
     * TODO 服务分组
     */
    private String serviceGroup;

    /**
     * 获取服务的Key值，即name+version
     */
    public String getServiceKey(){
        return String.format("%s:%s", serviceName, serviceVersion);
    }
    /**
     * 获取服务的地址key, 即服务Key+Address
     */
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, port);
    }

    /**
     * 获取服务地址
     */
    public String getServiceAddress(){
        if(!StrUtil.contains(serviceHost, "http")){
            return String.format("http://%s:%s", serviceHost, port);
        }
        return String.format("%s:%s", serviceHost, port);
    }
}
