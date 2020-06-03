package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.loginHistory.LoginHistoryFilterItemRes;
import my.lsge.application.dto.loginHistory.LoginHistoryFilterReq;
import my.lsge.application.dto.loginHistory.LoginHistoryFilterRes;
import my.lsge.domain.dao.LoginHistoryDao;
import my.lsge.domain.entity.LoginHistory;
import my.lsge.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoginHistoryLogic extends BaseLogic {
    @Autowired
    private LoginHistoryDao loginHistoryDao;

    public LoginHistoryFilterRes filterByUser(LoginHistoryFilterReq req, Long userId) {
        validateUser(userId);

        req.normalize();
        LoginHistoryFilterRes res = new LoginHistoryFilterRes();
        long count = loginHistoryDao.filterByUserCount(req, userId);
        if (count > 0) {
            res.setCount(count);
            List<LoginHistory> items = loginHistoryDao.filterByUser(req, userId);
            if (!Utils.isNullOrEmpty(items)) {
                res.setResponses(items.stream().map(LoginHistoryFilterItemRes::by).collect(Collectors.toList()));
            }
        }
        res.setPaging(req);
        return res;
    }
}
