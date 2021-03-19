package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.PostShareModeEnum;
import my.lsge.domain.enums.PostStatusEnum;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    private boolean hasImage;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Size(max = 16)
    private PostShareModeEnum shareMode;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Size(max = 16)
    private PostStatusEnum status;

    private Long rootId;

    @Size(max = 255)
    private String shareTitle;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "post_reactive_users",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedUsers = new HashSet<>();

    public Post() {
        this.hasImage = false;
        this.shareMode = PostShareModeEnum.PUBLIC;
        this.status = PostStatusEnum.CREATED;
    }

    public Post(Long id, String title, String content, boolean hasImage, PostShareModeEnum shareMode,
                 PostStatusEnum status, Long rootId, String shareTitle) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hasImage = hasImage;
        this.shareMode = shareMode;
        this.status = status;
        this.rootId = rootId;
        this.shareTitle = shareTitle;
    }
}
