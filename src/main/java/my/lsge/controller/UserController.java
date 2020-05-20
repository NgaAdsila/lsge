package my.lsge.controller;

import my.lsge.application.dto.user.UserIdentityAvailability;
import my.lsge.application.dto.user.UserSummary;
import my.lsge.application.security.CurrentUser;
import my.lsge.application.security.UserPrincipal;
import my.lsge.domain.logic.UserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {
    @Autowired
    private UserLogic userLogic;

    @GetMapping("/current_user")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getName()
        );
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam("username") String username) {
        return userLogic.checkUsernameAvailability(username);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam("email") String email) {
        return userLogic.checkEmailAvailability(email);
    }
}
