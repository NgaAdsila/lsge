package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitChatroomReq {

    private long userId;

    private String name;
}
