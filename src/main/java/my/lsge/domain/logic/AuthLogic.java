package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.common.Const;
import my.lsge.application.dto.ApiResponse;
import my.lsge.application.dto.auth.*;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.FormValidationException;
import my.lsge.application.security.JwtTokenProvider;
import my.lsge.application.security.UserPrincipal;
import my.lsge.domain.entity.LoginHistory;
import my.lsge.domain.entity.Role;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.UserRoleEnum;
import my.lsge.domain.repository.LoginHistoryRepository;
import my.lsge.domain.repository.RoleRepository;
import my.lsge.util.ObjectUtils;
import my.lsge.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AuthLogic extends BaseLogic {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public ResponseEntity<?> authenticateUser(LoginReq req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsernameOrEmail(),
                        req.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = getCurrentUser(authentication.getPrincipal());
        if (user == null || user.isDeleted()) {
            return new ResponseEntity(new ApiResponse(false, language.getString("valid.user.is_not_existed")),
                    HttpStatus.NOT_FOUND);
        }

        logSignedInHistory(user, req.getBrowser());

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationRes(authentication.getPrincipal(), jwt, user));
    }

    private User getCurrentUser(Object principal) {
        if (Utils.isNullOrEmptyObject(principal)) {
            return null;
        }
        try {
            Map<String, Object> principalMap = ObjectUtils.getFieldNamesAndValues(principal, false);
            Long userId = Long.parseLong(principalMap.get("id").toString());
            return userRepository.getOne(userId);
        } catch (Exception e) {
            log.error(String.format(language.getString("login.get_user_error"), e));
        }
        return null;
    }

    private void logSignedInHistory(User user, String browser) {
        try {
            if (user == null || user.getId() == null) {
                return;
            }
            InetAddress localMachine = InetAddress.getLocalHost();
            LoginHistory loginHistory = new LoginHistory(user, localMachine.getHostAddress(), browser);
            loginHistoryRepository.save(loginHistory);
        } catch (UnknownHostException e) {
            log.error(String.format(language.getString("login.save_history.error"), e));
        }
    }

    public ResponseEntity<?> registerUser(SignUpReq req) {
        req.normalize();
        if(userRepository.existsByUsername(req.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, language.getString("valid.user.username.is_existed")),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(req.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, language.getString("valid.user.email.is_existed")),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(req.getName(), req.getUsername(),
                req.getEmail(), req.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(UserRoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException(language.getString("valid.user.role_not_set")));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, language.getString("register.message.successfully")));
    }

    public ResponseEntity<?> refreshToken(RefreshTokenReq req) {
        validateUser(req.getId());
        String jwt = tokenProvider.generateTokenByUserId(req.getId());
        return ResponseEntity.ok(new ApiResponse(true, jwt));
    }

    public ForgetPasswordRes forgetPassword(ForgetPasswordReq req) {
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail());
        if (!userOpt.isPresent()) {
            return new ForgetPasswordRes(null, new ResponseEntity(
                    new ApiResponse(false, language.getString("valid.user.email.is_not_existed")),
                    HttpStatus.NOT_FOUND));
        }
        User user = userOpt.get();
        if (user.isDeleted()) {
            return new ForgetPasswordRes(null, new ResponseEntity(
                    new ApiResponse(false, language.getString("valid.user.is_not_existed")),
                    HttpStatus.NOT_FOUND));
        }

        String token = tokenProvider.generateTokenByUserIdAndExpiredTime(user.getId(), Const.ONE_DAY_TIME_IN_MS);
        user.setResetPasswordToken(token);
        user = userRepository.save(user);

        return new ForgetPasswordRes(user, ResponseEntity.ok(
                new ApiResponse(false, language.getString("forget_password.save.successfully"))));
    }

    public ResponseEntity<?> initResetPassword(InitResetPasswordReq req) {
        User user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null || user.isDeleted() || user.getResetPasswordToken() == null ||
                !user.getResetPasswordToken().equalsIgnoreCase(req.getToken())) {
            return new ResponseEntity(new ApiResponse(false, null), HttpStatus.FORBIDDEN);
        }

        if (!tokenProvider.validateToken(req.getToken())) {
            return new ResponseEntity(new ApiResponse(
                    false, language.getString("reset_password.token.invalid")), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new ApiResponse(true, "OK"));
    }

    public ForgetPasswordRes resetPassword(ResetPasswordReq req) {
        User user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null || user.isDeleted() || user.getResetPasswordToken() == null ||
                !user.getResetPasswordToken().equalsIgnoreCase(req.getToken())) {
            return new ForgetPasswordRes(null, new ResponseEntity(
                    new ApiResponse(false, null), HttpStatus.FORBIDDEN));
        }

        if (!tokenProvider.validateToken(req.getToken())) {
            return new ForgetPasswordRes(null, new ResponseEntity(new ApiResponse(
                    false, language.getString("reset_password.token.invalid")), HttpStatus.BAD_REQUEST));
        }
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setResetPasswordToken(null);
        user = userRepository.save(user);
        return new ForgetPasswordRes(user, ResponseEntity.ok(
                new ApiResponse(false, language.getString("reset_password.save.successfully"))));
    }
}
