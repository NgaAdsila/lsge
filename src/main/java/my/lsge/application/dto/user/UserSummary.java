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
    private String color;
    private String avatar;

    public UserSummary(Long id, String username, String name, String email, String color, String avatar) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.color = color;
        this.avatar = avatar;
    }
}
