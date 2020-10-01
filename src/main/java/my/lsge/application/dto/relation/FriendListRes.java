package my.lsge.application.dto.relation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FriendListRes {
    private List<FriendItemRes> friends;

    private List<FriendItemRes> requestedFriends;

    public FriendListRes() {
        this.friends = new ArrayList<>();
        this.requestedFriends = new ArrayList<>();
    }
}
