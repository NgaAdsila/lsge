package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class ChatroomUserReq {
    private Long id;

    @Size(max = 100)
    private String nickname;
}
