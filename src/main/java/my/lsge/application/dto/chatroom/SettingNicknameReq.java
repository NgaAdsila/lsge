package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SettingNicknameReq {

    @Valid
    @NotEmpty
    private List<ChatroomUserReq> userList;

    public SettingNicknameReq() {
        this.userList = new ArrayList<>();
    }
}
