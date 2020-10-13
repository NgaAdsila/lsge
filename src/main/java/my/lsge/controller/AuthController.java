package my.lsge.controller;

import my.lsge.application.dto.auth.*;
import my.lsge.domain.logic.AuthLogic;
import my.lsge.domain.logic.MailLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

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

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenReq req) {
        return authLogic.refreshToken(req);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetPasswordReq req)
            throws UnsupportedEncodingException {
        ForgetPasswordRes res = authLogic.forgetPassword(req);
        mailLogic.sendForgetPasswordMail(res);
        return res.getResponse();
    }

    @PostMapping("/init-reset-password")
    public ResponseEntity<?> initResetPassword(@Valid @RequestBody InitResetPasswordReq req) {
        return authLogic.initResetPassword(req);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordReq req) {
        ForgetPasswordRes res = authLogic.resetPassword(req);
        mailLogic.sendResetPasswordSuccessMail(res);
        return res.getResponse();
    }
}
