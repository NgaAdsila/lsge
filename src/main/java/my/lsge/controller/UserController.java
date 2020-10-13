package my.lsge.controller;

import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.OptionRes;
import my.lsge.application.dto.admin.user.UserFilterReq;
import my.lsge.application.dto.admin.user.UserFilterRes;
import my.lsge.application.dto.user.*;
import my.lsge.application.security.CurrentUser;
import my.lsge.application.security.UserPrincipal;
import my.lsge.domain.logic.UserLogic;
import my.lsge.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

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
    public ListObjectRes<String> getRole(@CurrentUser UserPrincipal currentUser) {
        ListObjectRes<String> res = new ListObjectRes<>();
        Collection<? extends GrantedAuthority> auths = currentUser == null ? null : currentUser.getAuthorities();
        if (!Utils.isNullOrEmptyObject(auths)) {
            res.setResponses(auths.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        }
        return res;
    }

    @RequestMapping(value = "/update",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.PUT
    )
    public UserSummary update(@Valid UpdatingUserReq req,
                              @RequestParam(value ="avatar", required=false) MultipartFile avatar) {
        return userLogic.update(req, avatar, getUserId());
    }

    @PutMapping("/change-password")
    public UserSummary changePassword(@Valid @RequestBody ChangingPasswordReq req) {
        return userLogic.changePassword(req, getUserId());
    }

    @GetMapping("/user-list")
    public ListObjectRes<UserWithNameItemRes> getUserList(FindUserReq req) {
        return userLogic.getUserList(req, getUserId());
    }

    @PostMapping("/filter")
    public UserFilterRes filter(@RequestBody UserFilterReq req) {
        return userLogic.filter(req, getUserId());
    }

    @GetMapping("/role-options")
    public ListObjectRes<OptionRes> getRoleOptions() {
        return userLogic.getRoleOptions(getUserId());
    }
}
