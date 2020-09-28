package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "message_tracking_statuses")
public class MessageTrackingStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_id")
    private long chatroomId;

    @Column(name = "message_id")
    private long messageId;

    @Column(name = "user_id")
    private long userId;

    private boolean isSeen;

    public MessageTrackingStatus() {
        this.isSeen = false;
    }

    public MessageTrackingStatus(Long id, Long chatroomId, Long messageId, Long userId, boolean isSeen) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.messageId = messageId;
        this.userId = userId;
        this.isSeen = isSeen;
    }
}
