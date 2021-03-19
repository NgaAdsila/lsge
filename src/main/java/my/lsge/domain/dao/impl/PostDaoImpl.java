package my.lsge.domain.dao.impl;

import my.lsge.application.dto.post.PostFilterReq;
import my.lsge.domain.dao.PostDao;
import my.lsge.domain.entity.Post;
import my.lsge.domain.enums.PostShareModeEnum;
import my.lsge.domain.enums.PostStatusEnum;
import my.lsge.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class PostDaoImpl extends BaseDaoImpl<Post> implements PostDao {

    @Override
    public long filterCount(PostFilterReq req, List<Long> friendIds, Long userId) {
        String condition = buildFilterCondition(req, friendIds, userId);
        Query query = entityManager.createQuery(String.format("SELECT COUNT(p.id) FROM Post p %s", condition));
        return (long) query.getSingleResult();
    }

    private String buildFilterCondition(PostFilterReq req, List<Long> friendIds, Long userId) {
        String condition = "WHERE p.isDeleted = FALSE ";
        condition += buildFilterConditionByRole(friendIds, userId);

        if (StringUtils.isNotBlank(req.getKeyword())) {
            condition += " AND (p.title LIKE '%" + req.getKeyword()+ "%'"
                    + " OR p.content LIKE '%" + req.getKeyword() + "%'"
                    + " OR (p.rootId IS NOT NULL AND p.shareTitle LIKE '%" + req.getKeyword() + "%')) ";
        }

        return condition;
    }

    private String buildFilterConditionByRole(List<Long> friendIds, Long userId) {
        String orConditions = String.format("p.createdBy = %d OR p.shareMode = '%s' ",
                userId, PostShareModeEnum.PUBLIC.name());
        if (!Utils.isNullOrEmpty(friendIds)) {
            orConditions += String.format(" OR (p.shareMode = '%s' " +
                    "AND p.createdBy IN (" + Utils.joinList(friendIds, ", ") + ")) ",
                    PostShareModeEnum.FRIEND.name());
        }
        return " AND (" + orConditions + ") ";
    }

    @Override
    public List<Post> filter(PostFilterReq req, List<Long> friendIds, Long userId) {
        String lastIdCondition = req.getLastId() < 0 ? "" : " AND p.id < " + req.getLastId();
        String query = String.format("SELECT p " +
                        "FROM Post p %s " +
                        "ORDER BY p.%s %s ",
                buildFilterCondition(req, friendIds, userId) + lastIdCondition,
                req.getSortBy(),
                req.getSortType());
        return entityManager.createQuery(query, Post.class)
                .setMaxResults(req.getLimit())
                .getResultList();
    }
}
