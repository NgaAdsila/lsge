package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.ChatroomUserStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "chatroom_users")
public class ChatroomUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 100)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ChatroomUserStatusEnum status;

    public ChatroomUser() {
        this.status = ChatroomUserStatusEnum.JOINING;
    }

    public ChatroomUser(Long id, Chatroom chatroom, User user, String nickname, ChatroomUserStatusEnum status) {
        this.id = id;
        this.chatroom = chatroom;
        this.user = user;
        this.nickname = nickname;
        this.status = status;
    }
}
