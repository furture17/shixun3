package sx5.service;

import com.baomidou.mybatisplus.extension.service.IService;
import sx5.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    void upRole(String userAccount);

    ArrayList<User> getUsers(String userAccount);
}