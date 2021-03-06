package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpReq {
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public void normalize() {
        if (StringUtils.isNotBlank(this.email)) {
            this.email = this.email.trim().toLowerCase();
        }
        if (StringUtils.isNotBlank(this.username)) {
            this.username = this.username.trim().toLowerCase();
        }
    }
}
