package my.lsge.domain.logic;

import my.lsge.application.dto.user.UserIdentityAvailability;
import my.lsge.application.dto.user.UserSummary;
import my.lsge.application.security.UserPrincipal;
import org.springframework.stereotype.Component;

@Component
public class UserLogic extends BaseLogic {

    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailability = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailability);
    }

    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailability = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailability);
    }
}
