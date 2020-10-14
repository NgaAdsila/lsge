package my.lsge.application.dto.admin.user;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.User;

@Getter
@Setter
public class UserPasswordRes {

    private User user;

    private String rawPassword;

    public UserPasswordRes() {

    }

    public UserPasswordRes(User user, String rawPassword) {
        this.user = user;
        this.rawPassword = rawPassword;
    }
}
