package my.lsge.application.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdatingUserReq {
    private int id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @Size(max = 16)
    private String color;

    private String avatar;

    public void normalize() {
        if (StringUtils.isNotBlank(this.email)) {
            this.email = this.email.trim().toLowerCase();
        }
        if (StringUtils.isNotBlank(this.color)) {
            this.color = this.color.trim().toUpperCase();
        }
    }
}
