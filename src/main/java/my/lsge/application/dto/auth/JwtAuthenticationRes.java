package my.lsge.application.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationRes {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationRes(String accessToken) {
        this.accessToken = accessToken;
    }
}
