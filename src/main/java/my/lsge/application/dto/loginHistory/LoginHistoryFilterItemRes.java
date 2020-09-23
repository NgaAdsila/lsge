package my.lsge.application.dto.loginHistory;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.common.Const;
import my.lsge.domain.entity.LoginHistory;
import my.lsge.util.Utils;

@Getter
@Setter
public class LoginHistoryFilterItemRes {
    private Long id;
    private Long userId;
    private String username;
    private String userFullName;
    private String ipAddress;
    private String browser;
    private String createdAt;

    public static LoginHistoryFilterItemRes by(LoginHistory loginHistory) {
        LoginHistoryFilterItemRes item = new LoginHistoryFilterItemRes();
        item.setId(loginHistory.getId());
        item.setUserId(loginHistory.getUser().getId());
        item.setUsername(loginHistory.getUser().getUsername());
        item.setUserFullName(loginHistory.getUser().getName());
        item.setIpAddress(loginHistory.getIpAddress());
        item.setBrowser(loginHistory.getBrowser());
        item.setCreatedAt(Utils.formatDateTime(loginHistory.getCreatedAt(), Const.DISPLAY_DATE_TIME_FORMAT));
        return item;
    }
}
