package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.dto.user.UserSummary;
import my.lsge.domain.entity.Comment;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.CommentStatusEnum;
import my.lsge.util.Utils;

import java.util.List;

@Getter
@Setter
public class PostCommentRes {
    private Long id;
    private String message;
    private CommentStatusEnum status;
    private UserSummary user;
    private Long createdAt;
    private Long modifiedAt;

    public static PostCommentRes by(Comment comment, List<User> commentUsers) {
        PostCommentRes res = new PostCommentRes();
        res.setId(comment.getId());
        res.setMessage(comment.getMessage());
        res.setStatus(comment.getStatus());
        res.setUser(UserSummary.byFromList(comment.getCreatedBy(), commentUsers));
        res.setCreatedAt(Utils.parseDateTimeToMilliSecond(comment.getCreatedAt()));
        res.setModifiedAt(Utils.parseDateTimeToMilliSecond(comment.getModifiedAt()));
        return res;
    }
}
