package my.lsge.domain.dao.impl;

import my.lsge.application.common.Const;
import my.lsge.application.dto.loginHistory.LoginHistoryFilterReq;
import my.lsge.domain.dao.LoginHistoryDao;
import my.lsge.domain.entity.LoginHistory;
import my.lsge.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class LoginHistoryDaoImpl extends BaseDaoImpl<LoginHistory> implements LoginHistoryDao {

    @Override
    public long filterByUserCount(LoginHistoryFilterReq req, long userId) {
        String condition = buildFilterByUserCondition(req, userId);
        Query query = entityManager.createQuery(String.format("SELECT COUNT(lh.id) FROM LoginHistory lh %s", condition));
        return (long) query.getSingleResult();
    }

    private String buildFilterByUserCondition(LoginHistoryFilterReq req, long userId) {
        String condition = " WHERE lh.user.id = " + userId;
        if (StringUtils.isNotBlank(req.getKeyword())) {
            condition += " AND (lh.ipAddress LIKE '%" + req.getKeyword() + "%' OR lh.browser LIKE '%" + req.getKeyword() + "%')";
        }
        if (req.getTimeFrom() != null && req.getTimeTo() != null) {
            if (req.getTimeFrom().isAfter(req.getTimeTo())) {
                condition += " AND 1 = 0 ";
            } else {
                condition += String.format(" AND DATE(lh.createdAt) BETWEEN '%s' AND '%s' ",
                        Utils.formatDate(req.getTimeFrom(), Const.DB_DATE_FORMAT),
                        Utils.formatDate(req.getTimeTo(), Const.DB_DATE_FORMAT));
            }
        } else if (req.getTimeFrom() != null) {
            condition += String.format(" AND DATE(lh.createdAt) >= '%s' ",
                    Utils.formatDate(req.getTimeFrom(), Const.DB_DATE_FORMAT));
        } else if (req.getTimeTo() != null) {
            condition += String.format(" AND DATE(lh.createdAt) <= '%s' ",
                    Utils.formatDate(req.getTimeTo(), Const.DB_DATE_FORMAT));
        }
        return condition;
    }

    @Override
    public List<LoginHistory> filterByUser(LoginHistoryFilterReq req, long userId) {
        String query = String.format("SELECT lh " +
                "FROM LoginHistory lh %s " +
                "ORDER BY lh.%s %s ",
                buildFilterByUserCondition(req, userId),
                req.getSortBy(),
                req.getSortType());
        return entityManager.createQuery(query, LoginHistory.class)
                .setFirstResult((req.getPage() - 1) * req.getLimit())
                .setMaxResults(req.getLimit())
                .getResultList();
    }
}
