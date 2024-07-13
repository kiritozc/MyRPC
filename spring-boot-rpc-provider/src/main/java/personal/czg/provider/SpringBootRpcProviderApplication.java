package personal.czg.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import personal.czg.rpc.starter.annotation.EnableRpc;

@SpringBootApplication
@EnableRpc
public class SpringBootRpcProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRpcProviderApplication.class, args);
    }


}
