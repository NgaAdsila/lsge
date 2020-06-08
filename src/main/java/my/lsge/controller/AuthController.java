package my.lsge.controller;

import my.lsge.application.dto.auth.LoginReq;
import my.lsge.application.dto.auth.SignUpReq;
import my.lsge.domain.logic.AuthLogic;
import my.lsge.domain.logic.MailLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthLogic authLogic;

    @Autowired
    private MailLogic mailLogic;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginReq req) {
        return authLogic.authenticateUser(req);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpReq req) {
        ResponseEntity<?> res = authLogic.registerUser(req);
        mailLogic.sendRegisterMail(req);
        return res;
    }
}
