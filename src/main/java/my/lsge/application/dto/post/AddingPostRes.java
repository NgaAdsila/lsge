package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.Post;
import my.lsge.domain.entity.Relationship;
import my.lsge.domain.enums.PostShareModeEnum;
import my.lsge.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class AddingPostRes {
    private PostRes post;
    private List<Long> friendIds = new ArrayList<>();

    public static AddingPostRes by(Post post, List<Relationship> friends) {
        AddingPostRes res = new AddingPostRes();
        res.setPost(PostRes.by(post));
        if (post.getShareMode().equals(PostShareModeEnum.FRIEND) && !Utils.isNullOrEmpty(friends)) {
            res.setFriendIds(friends.stream()
                    .map(f -> f.getId().getReqUserId().equals(post.getCreatedBy())
                            ? f.getId().getRecUserId()
                            : f.getId().getReqUserId())
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList()));
        }
        return res;
    }
}
