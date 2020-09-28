package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.Chatroom;
import my.lsge.domain.entity.ChatroomUser;
import my.lsge.domain.enums.ChatroomTypeEnum;
import my.lsge.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChatroomRes {
    private Long id;
    private String name;
    private ChatroomTypeEnum type;
    private List<ChatroomUserRes> users;
    private MessageRes lastMessage;
    private List<MessageRes> messages;

    public ChatroomRes() {
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public static ChatroomRes by(Chatroom chatroom, boolean hasMessage, List<ChatroomUser> userList) {
        ChatroomRes res = new ChatroomRes();
        res.setId(chatroom.getId());
        res.setName(chatroom.getName());
        res.setType(chatroom.getType());
        if (hasMessage && !Utils.isNullOrEmpty(chatroom.getMessages())) {
            List<MessageRes> messages = chatroom.getMessages().stream()
                    .map(MessageRes::by)
                    .collect(Collectors.toList());
            res.messages = messages;
            res.setLastMessage(messages.get(messages.size() - 1));
        }
        if (!Utils.isNullOrEmpty(userList)) {
            res.setUsers(userList.stream().map(ChatroomUserRes::by).collect(Collectors.toList()));
        }
        return res;
    }
}
