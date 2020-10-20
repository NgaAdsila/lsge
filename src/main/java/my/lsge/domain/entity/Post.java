package my.lsge.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.lsge.domain.enums.PostShareModeEnum;
import my.lsge.domain.enums.PostStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    public Post() {
        this.hasImage = false;
        this.shareMode = PostShareModeEnum.PUBLIC;
        this.status = PostStatusEnum.CREATED;
    }

    private Post(Long id, String title, String content, boolean hasImage, PostShareModeEnum shareMode,
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
