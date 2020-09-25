package my.lsge.application.dto.user;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.Relationship;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.RelationShipStatusEnum;

import java.util.List;

@Getter
@Setter
public class UserWithNameItemRes {
    private Long id;
    private String name;
    private boolean hasFriendReq;
    private RelationShipStatusEnum status;
    private boolean canApprove;

    public UserWithNameItemRes() {
        this.hasFriendReq = false;
        this.canApprove = false;
    }

    public static UserWithNameItemRes by(User user, List<Relationship> relationships) {
        UserWithNameItemRes res = new UserWithNameItemRes();
        res.setId(user.getId());
        res.setName(user.getName());

        relationships.stream()
                .filter(r -> r.getId().getReqUserId().equals(user.getId()) ||
                        r.getId().getRecUserId().equals(user.getId()))
                .findFirst()
                .ifPresent(r -> {
                    res.setHasFriendReq(true);
                    res.setStatus(r.getStatus());
                    res.setCanApprove(r.getStatus().equals(RelationShipStatusEnum.PENDING)
                            && r.getId().getReqUserId().equals(user.getId()));
                });
        return res;
    }
}
