package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.ChatroomUser;
import my.lsge.domain.entity.Message;
import my.lsge.domain.enums.MessageTypeEnum;
import my.lsge.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MessageRes {
    private Long id;
    private Long chatroomId;
    private String message;
    private MessageTypeEnum type;
    private Long createdBy;
    private Long createdAt;
    private List<MessageIsSeenRes> statuses;

    public MessageRes() {
        this.statuses = new ArrayList<>();
    }

    public static MessageRes by(Message message) {
        MessageRes res = new MessageRes();
        res.setId(message.getId());
        res.setChatroomId(message.getChatroomId());
        res.setMessage(message.getMessage());
        res.setType(message.getType());
        res.setCreatedBy(message.getCreatedBy());
        res.setCreatedAt(Utils.parseDateTimeToMilliSecond(message.getCreatedAt()));
        if (!Utils.isNullOrEmpty(message.getStatuses())) {
            res.setStatuses(message.getStatuses().stream()
                .map(MessageIsSeenRes::by)
                .collect(Collectors.toList()));
        }
        return res;
    }
}
