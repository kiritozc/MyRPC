package personal.czg.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SpringBootRpcConsumerApplicationTests {
    @Resource
    ExampleConsumer exampleConsumer;
    @Test
    void contextLoads() {
        exampleConsumer.test2();
        exampleConsumer.test();
    }

}
