package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ForgetPasswordReq {

    @NotBlank
    private String email;
}
