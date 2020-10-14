package my.lsge.application.dto.admin.user;

import lombok.Getter;
import lombok.Setter;
import my.lsge.util.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdatingUserListReq {
    private List<Long> userIds;

    public UpdatingUserListReq() {
        this.userIds = new ArrayList<>();
    }

    public void normalize() {
        if (!Utils.isNullOrEmpty(this.userIds)) {
            this.userIds = Utils.removeNull(this.userIds);
        }
    }
}
