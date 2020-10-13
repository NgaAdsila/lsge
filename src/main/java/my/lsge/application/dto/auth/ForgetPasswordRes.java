package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.User;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ForgetPasswordRes {
    private User user;

    private ResponseEntity<?> response;

    public ForgetPasswordRes(User user, ResponseEntity<?> response) {
        this.user = user;
        this.response = response;
    }
}
