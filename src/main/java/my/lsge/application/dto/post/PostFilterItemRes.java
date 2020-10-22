package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.lsge.application.dto.user.UserSummary;
import my.lsge.domain.entity.Comment;
import my.lsge.domain.entity.Post;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.PostStatusEnum;
import my.lsge.util.Utils;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class PostFilterItemRes {
    private Long id;
    private String title;
    private String content;
    private PostRootRes root;
    private String shareTitle;
    private Set<UserSummary> likedUsers = new HashSet<>();
    private List<PostCommentRes> comments = new ArrayList<>();
    private UserSummary user;
    private PostStatusEnum status;
    private Long createdAt;
    private Long modifiedAt;

    public static PostFilterItemRes by(Post post, List<Comment> comments, List<User> userList, List<Post> roots) {
        PostFilterItemRes res = new PostFilterItemRes();
        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        if (!post.getLikedUsers().isEmpty()) {
            res.setLikedUsers(post.getLikedUsers().stream()
                    .map(u -> new UserSummary(u.getId(), u.getUsername(), u.getName(),
                            u.getEmail(), u.getColor(), u.getAvatar()))
                    .collect(Collectors.toSet()));
        }
        if (!Utils.isNullOrEmpty(comments)) {
            res.setComments(comments.stream()
                    .filter(c -> post.getId().equals(c.getReferenceId()))
                    .map(c -> PostCommentRes.by(c, userList))
                    .collect(Collectors.toList()));
        }
        if (post.getRootId() != null) {
            res.setRoot(PostRootRes.by(post.getRootId(), roots, userList));
        }
        post.setStatus(post.getStatus());
        res.setUser(UserSummary.byFromList(post.getCreatedBy(), userList));
        res.setCreatedAt(Utils.parseDateTimeToMilliSecond(post.getCreatedAt()));
        res.setModifiedAt(Utils.parseDateTimeToMilliSecond(post.getModifiedAt()));
        return res;
    }
}
