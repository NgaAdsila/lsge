package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.Post;
import my.lsge.domain.enums.PostShareModeEnum;
import my.lsge.domain.enums.PostStatusEnum;
import my.lsge.util.Utils;

@Getter
@Setter
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

    public static PostRes by(Post post) {
        PostRes res = new PostRes();
        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setShareMode(post.getShareMode());
        res.setRootId(post.getRootId());
        res.setStatus(post.getStatus());
        res.setCreatedBy(post.getCreatedBy());
        res.setCreatedAt(Utils.parseDateTimeToMilliSecond(post.getCreatedAt()));
        res.setModifiedAt(Utils.parseDateTimeToMilliSecond(post.getModifiedAt()));
        return res;
    }
}
