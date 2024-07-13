package personal.czg.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import personal.czg.rpc.starter.annotation.EnableRpc;

import javax.annotation.Resource;

@SpringBootApplication
@EnableRpc(needServer = false)
public class SpringBootRpcConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootRpcConsumerApplication.class, args);
    }

}
