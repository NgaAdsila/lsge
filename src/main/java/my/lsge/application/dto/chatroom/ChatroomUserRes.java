package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.ChatroomUser;

@Getter
@Setter
public class ChatroomUserRes {
    private Long id;
    private String name;
    private String nickname;
    private String color;
    private String avatar;

    public static ChatroomUserRes by(ChatroomUser user) {
        ChatroomUserRes res = new ChatroomUserRes();
        res.setId(user.getUser().getId());
        res.setName(user.getUser().getName());
        res.setNickname(user.getNickname());
        res.setColor(user.getUser().getColor());
        res.setAvatar(user.getUser().getAvatar());
        return res;
    }
}
