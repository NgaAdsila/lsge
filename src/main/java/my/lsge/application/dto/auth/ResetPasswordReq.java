package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordReq {

    @NotBlank
    private String email;

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
