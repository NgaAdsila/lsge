package my.lsge.application.dto.admin.user;

import lombok.Getter;
import lombok.Setter;
import my.lsge.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class UpdatingUserRoleReq {

    private List<UserRoleReq> userRoleList;

    public UpdatingUserRoleReq() {
        this.userRoleList = new ArrayList<>();
    }

    public void normalize() {
        if (!Utils.isNullOrEmpty(this.userRoleList)) {
            this.userRoleList = this.userRoleList.stream()
                    .filter(Objects::nonNull)
                    .peek(ur -> {
                        if (!Utils.isNullOrEmpty(ur.getRoles())) {
                            ur.setRoles(Utils.removeNull(ur.getRoles()));
                        }
                    })
                    .collect(Collectors.toList());
        }
    }
}
