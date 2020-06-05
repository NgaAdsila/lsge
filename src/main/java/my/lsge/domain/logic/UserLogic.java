package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.user.ChangingPasswordReq;
import my.lsge.application.dto.user.UpdatingUserReq;
import my.lsge.application.dto.user.UserIdentityAvailability;
import my.lsge.application.dto.user.UserSummary;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.FormValidationException;
import my.lsge.application.security.UserPrincipal;
import my.lsge.domain.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserLogic extends BaseLogic {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailability = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailability);
    }

    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailability = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailability);
    }

    public UserSummary update(UpdatingUserReq req, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);
        req.normalize();
        validateUpdatingReq(req, user);

        mapUpdatingReqToModel(req, user);
        userRepository.saveAndFlush(user);
        return new UserSummary(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail());
    }

    private void mapUpdatingReqToModel(UpdatingUserReq req, User user) {
        user.setName(req.getName());
        if (!user.getEmail().equalsIgnoreCase(req.getEmail())) {
            user.setEmail(req.getEmail());
        }
    }

    private void validateUpdatingReq(UpdatingUserReq req, User user) {
        if (req.getId() != user.getId()) {
            throw new ForbiddenException();
        }
        req.normalize();
        if (!user.getEmail().equalsIgnoreCase(req.getEmail()) && userRepository.existsByEmail(req.getEmail())) {
            throw new FormValidationException("Email Address already in use!");
        }
    }

    public UserSummary changePassword(ChangingPasswordReq req, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);
        validateChangingPasswordReq(req, user);

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.saveAndFlush(user);
        return new UserSummary(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail());
    }

    private void validateChangingPasswordReq(ChangingPasswordReq req, User user) {
        if (StringUtils.isBlank(req.getOldPassword()) ||
                !passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new FormValidationException("Old password is incorrect!");
        }
    }
}
