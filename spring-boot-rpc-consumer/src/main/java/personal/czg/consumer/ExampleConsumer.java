package personal.czg.consumer;

import org.springframework.stereotype.Service;
import personal.czg.common.Model.User;
import personal.czg.common.Service.MyService;
import personal.czg.common.Service.UserService;
import personal.czg.rpc.starter.annotation.RpcReference;

@Service
public class ExampleConsumer {

    /**
     * 使用 Rpc 框架注入
     */
    @RpcReference
    private UserService userService;

    @RpcReference
    private MyService myService;

    /**
     * 测试方法
     */
    public void test() {
        User user = new User();
        user.setName("XiaoBai");
        User resultUser = userService.getUser(user);
//        System.out.println(resultUser.getName());
    }

    public void test2() {
        myService.myServiceMethod();
    }

}
