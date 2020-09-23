package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.ListObjectRes;
import my.lsge.domain.entity.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RelationshipLogic extends BaseLogic {

    public ListObjectRes<User> getFriendList(Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        ListObjectRes<User> res = new ListObjectRes<>();
        res.setResponses(user.friendList());
        return res;
    }
}
