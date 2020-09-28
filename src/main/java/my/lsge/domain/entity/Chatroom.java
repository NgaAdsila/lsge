package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.ChatroomStatusEnum;
import my.lsge.domain.enums.ChatroomTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chatrooms")
public class Chatroom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ChatroomTypeEnum type;

    private Long lastMessageId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ChatroomStatusEnum status;

    @OneToMany
    @JoinColumn(name = "chatroom_id")
    private List<Message> messages;

    public Chatroom() {
        this.status = ChatroomStatusEnum.CREATED;
    }

    public Chatroom(Long id, String name, ChatroomTypeEnum type, Long lastMessageId, ChatroomStatusEnum status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.lastMessageId = lastMessageId;
        this.status = status;
    }
}
