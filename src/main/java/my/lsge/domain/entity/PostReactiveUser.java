package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.PostReactiveUserTypeEnum;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "post_reactive_users")
public class PostReactiveUser extends BaseEntity {

    @EmbeddedId
    private PostReactiveUserId id;

    @MapsId(value = "postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @MapsId(value = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Size(max = 16)
    private PostReactiveUserTypeEnum type;

    public PostReactiveUser() {
        this.type = PostReactiveUserTypeEnum.LIKE;
    }

    public PostReactiveUser(PostReactiveUserId id, Post post, User user, PostReactiveUserTypeEnum type) {
        this.id = id;
        this.post = post;
        this.user = user;
        this.type = type;
        this.setCreatedAt(LocalDateTime.now());
        this.setModifiedAt(LocalDateTime.now());
    }
}
