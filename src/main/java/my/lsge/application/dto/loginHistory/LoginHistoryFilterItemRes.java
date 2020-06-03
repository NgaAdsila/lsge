package my.lsge.application.dto.loginHistory;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.entity.LoginHistory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class LoginHistoryFilterItemRes {
    private Long id;
    private Long userId;
    private String username;
    private String userFullName;
    private String ipAddress;
    private String createdAt;

    public static LoginHistoryFilterItemRes by(LoginHistory loginHistory) {
        LoginHistoryFilterItemRes item = new LoginHistoryFilterItemRes();
        item.setId(loginHistory.getId());
        item.setUserId(loginHistory.getUser().getId());
        item.setUsername(loginHistory.getUser().getUsername());
        item.setUserFullName(loginHistory.getUser().getName());
        item.setIpAddress(loginHistory.getIpAddress());
        item.setCreatedAt(loginHistory.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        return item;
    }
}
