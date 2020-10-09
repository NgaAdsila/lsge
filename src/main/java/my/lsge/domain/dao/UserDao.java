package my.lsge.domain.dao;

import my.lsge.application.dto.admin.user.UserFilterReq;
import my.lsge.application.dto.user.FindUserReq;
import my.lsge.domain.entity.Role;
import my.lsge.domain.entity.User;

import java.util.List;

public interface UserDao {

    List<User> findFriends(Long userId, Role role, FindUserReq req);

    long filterCount(UserFilterReq req, long userId);

    List<User> filter(UserFilterReq req, long userId);
}
