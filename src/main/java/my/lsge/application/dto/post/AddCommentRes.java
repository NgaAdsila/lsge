package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.lsge.domain.entity.Comment;
import my.lsge.domain.entity.Post;

@Getter
@Setter
@NoArgsConstructor
public class AddCommentRes extends PostRes {
    private Comment newComment;

    public AddCommentRes(Post post, Comment comment) {
        super(post);
        this.newComment = comment;
    }
}
