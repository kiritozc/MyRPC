package personal.czg.provider;

import personal.czg.common.Model.User;
import personal.czg.common.Service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        return user;
    }
}
