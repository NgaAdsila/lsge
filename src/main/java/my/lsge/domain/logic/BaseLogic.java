package my.lsge.domain.logic;

import my.lsge.application.exception.ForbiddenException;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.UserRoleEnum;
import my.lsge.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class BaseLogic {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    ResourceBundle language;

    void validateUser(Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);
    }

    void validateUser(User user) {
        if (user == null || user.isDeleted()) {
            throw new ForbiddenException();
        }
    }

    void validateUser(Long userId, UserRoleEnum role) {
        User user = userRepository.getOne(userId);
        validateUser(user, role);
    }

    void validateUser(User user, UserRoleEnum role) {
        if (user == null || user.isDeleted() || user.getRoles().stream().noneMatch(r -> r.getName().equals(role))) {
            throw new ForbiddenException();
        }
    }
}
