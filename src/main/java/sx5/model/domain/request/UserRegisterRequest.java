package sx5.model.domain.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户密码
     */
    private String checkPassword;
}
