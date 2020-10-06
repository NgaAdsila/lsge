package my.lsge.application.dto.relation;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.dto.chatroom.MessageRes;
import my.lsge.domain.entity.Message;
import my.lsge.domain.entity.User;

import java.util.Map;

@Getter
@Setter
public class FriendItemRes {
    private Long id;
    private String name;
    private MessageRes lastMessage;

    public static FriendItemRes by(User user) {
        FriendItemRes res = new FriendItemRes();
        res.setId(user.getId());
        res.setName(user.getName());
        return res;
    }

    public static FriendItemRes by(User user, Map<Long, Message> messageList) {
        FriendItemRes res = FriendItemRes.by(user);
        if (messageList != null && messageList.containsKey(user.getId())) {
            res.setLastMessage(MessageRes.by(messageList.get(user.getId())));
        }
        return res;
    }
}
