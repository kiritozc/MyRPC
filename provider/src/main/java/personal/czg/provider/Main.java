package personal.czg.provider;

import personal.czg.common.Service.UserService;
import personal.czg.rpc.RpcApplication;
import personal.czg.rpc.bootstrap.ProviderStrap;
import personal.czg.rpc.config.RegistryConfig;
import personal.czg.rpc.config.RpcConfig;
import personal.czg.rpc.constant.RpcConstant;
import personal.czg.rpc.model.ServiceMetaInfo;
import personal.czg.rpc.model.ServiceRegisterInfo;
import personal.czg.rpc.registry.LocalRegistry;
import personal.czg.rpc.registry.Registry;
import personal.czg.rpc.registry.RegistryFactory;
import personal.czg.rpc.server.HttpServer;
import personal.czg.rpc.server.VertexHttpServer;

import java.util.ArrayList;
import java.util.List;


/**
 * 注册中心负责给客户提供各种服务的地址，然后客户根据地址去找提供者，提供者从本地的注册中心，即服务库里给用户提供服务
 */

public class Main {
    public static void main(String[] args) {
        List<ServiceRegisterInfo<?>> serviceList = new ArrayList<>();
        serviceList.add(new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class));
        ProviderStrap.init(serviceList);
    }
}
