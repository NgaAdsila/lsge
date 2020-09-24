package my.lsge.application.dto.relation;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.User;

@Getter
@Setter
public class FriendItemRes {
    private Long id;
    private String name;

    public static FriendItemRes by(User user) {
        FriendItemRes res = new FriendItemRes();
        res.setId(user.getId());
        res.setName(user.getName());
        return res;
    }
}
