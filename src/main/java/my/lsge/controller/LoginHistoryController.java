package my.lsge.controller;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.loginHistory.LoginHistoryFilterReq;
import my.lsge.application.dto.loginHistory.LoginHistoryFilterRes;
import my.lsge.domain.logic.LoginHistoryLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/login-histories")
public class LoginHistoryController extends BaseController {
    @Autowired
    private LoginHistoryLogic loginHistoryLogic;

    @PostMapping("/filter-by-user")
    public LoginHistoryFilterRes filterByUser(@Valid @RequestBody LoginHistoryFilterReq req) {
        return loginHistoryLogic.filterByUser(req, getUserId());
    }
}
