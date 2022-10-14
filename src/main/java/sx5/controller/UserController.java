package sx5.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import sx5.common.BaseResponse;
import sx5.common.ErrorCode;
import sx5.common.ResultUtils;
import sx5.model.domain.User;
import sx5.model.domain.request.UserLoginRequest;
import sx5.model.domain.request.UserRegisterRequest;
import sx5.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "无法获取请求", "");
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "账号或密码存在空值", "");
        }

        long resultId = userService.userRegister(userAccount, userPassword, checkPassword);
        if (resultId == -1)
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"两次密码不一致", "");
        if (resultId == -2)
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "账号重复", "");
        else if(resultId == -3)
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统内部错误", "");

        return ResultUtils.success(resultId);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "账号或密码存在空值", "");
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        if (user == null)
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"用户名与密码不匹配","");
        return ResultUtils.success(user);
    }
}
