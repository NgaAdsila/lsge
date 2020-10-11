package my.lsge.application.dto.admin.user;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.common.Const;
import my.lsge.domain.entity.User;
import my.lsge.util.Utils;

import java.util.stream.Collectors;

@Getter
@Setter
public class UserFilterItemRes {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String roles;
    private boolean isDeleted;
    private String createdAt;

    public UserFilterItemRes() {
    }

    public static UserFilterItemRes by(User user) {
        UserFilterItemRes res = new UserFilterItemRes();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        if (!user.getRoles().isEmpty()) {
            res.setRoles(Utils.joinList(user.getRoles().stream().map(r -> r.getName().getTitle()).collect(Collectors.toList()), ", "));
        }
        res.setDeleted(user.isDeleted());
        res.setCreatedAt(Utils.formatDateTime(user.getCreatedAt(), Const.DISPLAY_DATE_TIME_FORMAT));
        return res;
    }
}
