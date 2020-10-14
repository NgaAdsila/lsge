package my.lsge.domain.dao.impl;

import my.lsge.application.dto.admin.user.UserFilterReq;
import my.lsge.application.dto.user.FindUserReq;
import my.lsge.domain.dao.UserDao;
import my.lsge.domain.entity.Role;
import my.lsge.domain.entity.User;
import my.lsge.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
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

    @Override
    public long filterCount(UserFilterReq req, long userId) {
        String condition = buildFilterCondition(req, userId);
        Query query = entityManager.createQuery("SELECT COUNT(DISTINCT u.id) FROM User u JOIN u.roles r " + condition);
        return (long) query.getSingleResult();
    }

    private String buildFilterCondition(UserFilterReq req, long userId) {
        String condition = " WHERE u.id <> " + userId;
        if (StringUtils.isNotBlank(req.getKeyword())) {
            condition += " AND (u.name LIKE '%" + req.getKeyword() + "%' " +
                    "OR u.username LIKE '%" + req.getKeyword() + "%' " +
                    "OR u.email LIKE '%" + req.getKeyword() + "%')";
        }
        if (!Utils.isNullOrEmpty(req.getRoles())) {
            condition += " AND r.id IN (" + Utils.joinList(req.getRoles(), ",") + ") ";
        }
        if (req.getStatus() != null) {
            condition += " AND u.isDeleted = " + req.getStatus();
        }
        return condition;
    }

    @Override
    public List<User> filter(UserFilterReq req, long userId) {
        String query = "SELECT DISTINCT u FROM User u JOIN u.roles r " + buildFilterCondition(req, userId) +
                " ORDER BY u.isDeleted ASC, u." + req.getSortBy() + " " + req.getSortType();
        return entityManager.createQuery(query, User.class)
                .setFirstResult((req.getPage() - 1) * req.getLimit())
                .setMaxResults(req.getLimit())
                .getResultList();
    }
}
