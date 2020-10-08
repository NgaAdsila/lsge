package my.lsge.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long chatroomId;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private Message message;

    @Column(name = "user_id")
    private Long userId;

    private boolean isSeen;

    public MessageTrackingStatus() {
        this.isSeen = false;
    }

    public MessageTrackingStatus(Long id, Long chatroomId, Message message, Long userId, boolean isSeen) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.message = message;
        this.userId = userId;
        this.isSeen = isSeen;
    }
}
