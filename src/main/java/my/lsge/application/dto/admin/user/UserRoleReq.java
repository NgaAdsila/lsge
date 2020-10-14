package my.lsge.application.dto.admin.user;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserRoleReq {
    private long id;

    private List<Long> roles;

    public UserRoleReq() {
        this.roles = new ArrayList<>();
    }
}
