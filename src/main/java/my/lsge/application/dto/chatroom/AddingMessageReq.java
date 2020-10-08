package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AddingMessageReq {

    private long chatroomId;

    @Size(max = 2000)
    @NotBlank
    private String message;

    public void normalize() {
        this.message = this.message.trim();
    }
}
