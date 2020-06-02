package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.security.UserPrincipal;

@Getter
@Setter
public class JwtAuthenticationRes {
    private Object user;
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationRes(Object currentUser, String accessToken) {
        this.user = currentUser;
        this.accessToken = accessToken;
    }
}
