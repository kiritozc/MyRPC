package personal.czg.consumer;

import personal.czg.common.Model.User;
import personal.czg.common.Service.UserService;
import personal.czg.rpc.bootstrap.ConsumerStrap;
import personal.czg.rpc.proxy.ServiceProxyFactory;

public class Main {
    public static void main(String[] args){
        ConsumerStrap.init();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("Li Bai");
        userService.getUser(user);
    }
}




