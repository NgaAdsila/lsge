package my.lsge.application.dto.chatroom;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.MessageTrackingStatus;

@Getter
@Setter
public class MessageIsSeenRes {
    private Long userId;
    private boolean isSeen;

    public static MessageIsSeenRes by(MessageTrackingStatus status) {
        MessageIsSeenRes res = new MessageIsSeenRes();
        res.setUserId(status.getUserId());
        res.setSeen(status.isSeen());
        return res;
    }
}
