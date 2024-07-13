package personal.czg;

import org.junit.Test;
import personal.czg.rpc.config.RpcConfig;

public class TestClass {
    @Test
    public void configTest(){
        RpcConfig rpcConfig =  personal.czg.rpc.RpcApplication.getRpcConfig();
        System.out.println(rpcConfig.toString());
    }
}
