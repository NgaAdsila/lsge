package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.User;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ForgetPasswordRes {
    private User user;

    private String rawPassword;

    private ResponseEntity<?> response;

    public ForgetPasswordRes(User user, String rawPassword, ResponseEntity<?> response) {
        this.user = user;
        this.rawPassword = rawPassword;
        this.response = response;
    }
}
