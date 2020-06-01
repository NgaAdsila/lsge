package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.security.UserPrincipal;

@Getter
@Setter
public class JwtAuthenticationRes {
    private Object currentUser;
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationRes(Object currentUser, String accessToken) {
        this.currentUser = currentUser;
        this.accessToken = accessToken;
    }
}
