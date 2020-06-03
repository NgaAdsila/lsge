package my.lsge.domain.dao;

import my.lsge.application.dto.loginHistory.LoginHistoryFilterReq;
import my.lsge.domain.entity.LoginHistory;

import java.util.List;

public interface LoginHistoryDao {
    long filterByUserCount(LoginHistoryFilterReq req, long userId);
    List<LoginHistory> filterByUser(LoginHistoryFilterReq req, long userId);
}
