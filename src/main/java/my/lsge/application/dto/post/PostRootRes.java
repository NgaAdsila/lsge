package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.dto.user.UserSummary;
import my.lsge.domain.entity.Post;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.PostStatusEnum;
import my.lsge.util.Utils;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class PostRootRes {
    private Long id;
    private String title;
    private String content;
    private UserSummary user;
    private PostStatusEnum status;
    private Long createdAt;
    private Long modifiedAt;

    public static PostRootRes by(Long rootId, List<Post> roots, List<User> userList) {
        Optional<Post> rootOpt = roots.stream().filter(r -> r.getId().equals(rootId)).findFirst();
        if (!rootOpt.isPresent()) {
            return null;
        }
        Post root = rootOpt.get();
        return by(root, userList);
    }

    public static PostRootRes by(Post root, List<User> userList) {
        PostRootRes res = new PostRootRes();
        res.setId(root.getId());
        res.setTitle(root.getTitle());
        res.setContent(root.getContent());
        res.setUser(UserSummary.byFromList(root.getCreatedBy(), userList));
        res.setStatus(root.getStatus());
        res.setCreatedAt(Utils.parseDateTimeToMilliSecond(root.getCreatedAt()));
        res.setModifiedAt(Utils.parseDateTimeToMilliSecond(root.getModifiedAt()));
        return res;
    }
}
