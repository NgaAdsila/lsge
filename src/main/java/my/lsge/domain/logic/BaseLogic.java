package my.lsge.domain.logic;

import my.lsge.application.exception.ForbiddenException;
import my.lsge.domain.entity.User;
import my.lsge.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseLogic {
    @Autowired
    protected UserRepository userRepository;

    void validateUser(Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);
    }

    void validateUser(User user) {
        if (user == null || user.isDeleted()) {
            throw new ForbiddenException();
        }
    }
}
