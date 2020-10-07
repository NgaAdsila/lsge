package my.lsge.controller;

import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.user.*;
import my.lsge.application.security.CurrentUser;
import my.lsge.application.security.UserPrincipal;
import my.lsge.domain.logic.UserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {
    @Autowired
    private UserLogic userLogic;

    @GetMapping("/current-user")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getName(),
                currentUser.getEmail(),
                currentUser.getColor(),
                currentUser.getAvatar()
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

    @GetMapping("/get-role")
    public String getRole() {
        return "USER";
    }

    @PutMapping("/update")
    public UserSummary update(@Valid @RequestBody UpdatingUserReq req) {
        return userLogic.update(req, getUserId());
    }

    @PutMapping("/change-password")
    public UserSummary changePassword(@Valid @RequestBody ChangingPasswordReq req) {
        return userLogic.changePassword(req, getUserId());
    }

    @GetMapping("/user-list")
    public ListObjectRes<UserWithNameItemRes> getUserList(FindUserReq req) {
        return userLogic.getUserList(req, getUserId());
    }
}
