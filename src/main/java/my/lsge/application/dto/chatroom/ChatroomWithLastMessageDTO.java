package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.Message;
import my.lsge.domain.entity.User;

@Getter
@Setter
public class ChatroomWithLastMessageDTO {
    private Long id;
    private Message lastMessage;
    private User user;

    public ChatroomWithLastMessageDTO() {

    }

    public ChatroomWithLastMessageDTO(Long id, Message lastMessage, User user) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.user = user;
    }
}
