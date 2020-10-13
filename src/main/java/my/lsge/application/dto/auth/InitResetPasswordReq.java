package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class InitResetPasswordReq {

    @NotBlank
    private String email;

    @NotBlank
    private String token;
}
