package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.lsge.domain.entity.Comment;
import my.lsge.domain.entity.Post;

@Getter
@Setter
@NoArgsConstructor
public class ReplyCommentRes extends PostRes {
    private Comment comment;

    public ReplyCommentRes(Post post, Comment comment) {
        super(post);
        this.comment = comment;
    }
}
