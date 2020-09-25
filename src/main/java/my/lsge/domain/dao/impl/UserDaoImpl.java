package my.lsge.domain.dao.impl;

import my.lsge.application.dto.user.FindUserReq;
import my.lsge.domain.dao.UserDao;
import my.lsge.domain.entity.Role;
import my.lsge.domain.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    @Override
    public List<User> findFriends(Long userId, Role role, FindUserReq req) {
        String query = "SELECT u FROM User u JOIN u.roles r WHERE 1 = 1 ";
        if (userId != null) {
            query += String.format(" AND u.id <> %s ", userId);
        }
        if (role != null) {
            query += String.format(" AND r.id <> %s ", role.getId());
        }
        if (StringUtils.isNotBlank(req.getKeyword())) {
            query += String.format(" AND (u.name LIKE '%%%s%%' OR u.email LIKE '%%%s%%') ", req.getKeyword(), req.getKeyword());
        }
        return entityManager.createQuery(query, User.class).getResultList();
    }
}
