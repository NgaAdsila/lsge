package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_id")
    private long chatroomId;

    @Size(max = 2000)
    private String message;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "message")
    private List<MessageTrackingStatus> statuses;

    public Message() {

    }

    public Message(Long id, Long chatroomId, String message) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.message = message;
    }
}
