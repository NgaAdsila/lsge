package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.lsge.domain.entity.Post;
import my.lsge.domain.enums.PostShareModeEnum;
import my.lsge.domain.enums.PostStatusEnum;
import my.lsge.util.Utils;

@Getter
@Setter
@NoArgsConstructor
public class PostRes {
    private Long id;
    private String title;
    private String content;
    private PostShareModeEnum shareMode;
    private Long rootId;
    private String shareTitle;
    private Long createdBy;
    private PostStatusEnum status;
    private Long createdAt;
    private Long modifiedAt;

    public PostRes(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.shareMode = post.getShareMode();
        this.rootId = post.getRootId();
        this.shareTitle = post.getShareTitle();
        this.status = post.getStatus();
        this.createdBy = post.getCreatedBy();
        this.createdAt = Utils.parseDateTimeToMilliSecond(post.getCreatedAt());
        this.modifiedAt = Utils.parseDateTimeToMilliSecond(post.getModifiedAt());
    }

    public static PostRes by(Post post) {
        return new PostRes(post);
    }
}
