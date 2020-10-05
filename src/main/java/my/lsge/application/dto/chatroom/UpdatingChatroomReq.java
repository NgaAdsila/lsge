package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdatingChatroomReq {

    @Size(max = 100)
    private String name;
}
