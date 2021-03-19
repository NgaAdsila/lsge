package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.dto.user.UserSummary;
import my.lsge.domain.entity.Comment;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.CommentStatusEnum;
import my.lsge.util.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PostCommentRes {
    private Long id;
    private String message;
    private CommentStatusEnum status;
    private UserSummary user;
    private Long createdAt;
    private Long modifiedAt;

    private List<PostCommentRes> repliedComments = new ArrayList<>();

    public static PostCommentRes by(Comment comment, List<Comment> comments, List<User> commentUsers) {
        List<PostCommentRes> repliedComments = comments.stream()
                .filter(c -> comment.getId().equals(c.getParentId()))
                .sorted(Comparator.comparingLong(Comment::getId))
                .map(c -> by(c, commentUsers))
                .collect(Collectors.toList());
        PostCommentRes res = by(comment, commentUsers);
        if (!Utils.isNullOrEmpty(repliedComments)) {
            res.setRepliedComments(repliedComments);
        }
        return res;
    }

    private static PostCommentRes by(Comment comment, List<User> commentUsers) {
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
