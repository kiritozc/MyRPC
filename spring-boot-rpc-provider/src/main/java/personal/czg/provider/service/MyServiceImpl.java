package personal.czg.provider.service;

import org.springframework.stereotype.Service;
import personal.czg.common.Service.MyService;
import personal.czg.rpc.starter.annotation.RpcService;

@Service
@RpcService
public class MyServiceImpl implements MyService {
    String name;
    public void myServiceMethod(){
        System.out.println("我的方法");
    }
}
