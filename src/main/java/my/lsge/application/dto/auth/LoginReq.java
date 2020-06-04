package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginReq {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    private String browser;
}
