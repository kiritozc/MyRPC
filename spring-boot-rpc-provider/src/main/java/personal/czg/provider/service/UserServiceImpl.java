package personal.czg.provider.service;

import org.springframework.stereotype.Service;
import personal.czg.common.Model.User;
import personal.czg.common.Service.UserService;
import personal.czg.rpc.starter.annotation.RpcService;


@RpcService
@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        return user;
    }
}
