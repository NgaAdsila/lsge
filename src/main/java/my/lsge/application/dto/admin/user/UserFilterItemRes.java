package my.lsge.application.dto.admin.user;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.Role;
import my.lsge.domain.entity.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserFilterItemRes {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Set<Role> roles;

    public UserFilterItemRes() {
        this.roles = new HashSet<>();
    }

    public static UserFilterItemRes by(User user) {
        UserFilterItemRes res = new UserFilterItemRes();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setRoles(user.getRoles());
        return res;
    }
}
