package my.lsge.application.dto.user;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.User;

@Getter
@Setter
public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private String email;

    public UserSummary(Long id, String username, String name, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }
}
