package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.user.*;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.FormValidationException;
import my.lsge.domain.dao.UserDao;
import my.lsge.domain.entity.Relationship;
import my.lsge.domain.entity.Role;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.UserRoleEnum;
import my.lsge.domain.repository.RelationshipRepository;
import my.lsge.domain.repository.RoleRepository;
import my.lsge.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserLogic extends FileLogic {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private UserDao userDao;

    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailability = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailability);
    }

    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailability = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailability);
    }

    public UserSummary update(UpdatingUserReq req, MultipartFile avatar, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);
        req.normalize();
        validateUpdatingReq(req, user);
        mapUpdatingReqToModel(req, user, uploadAvatar(avatar, user));
        userRepository.saveAndFlush(user);
        return new UserSummary(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getColor(),
                user.getAvatar());
    }

    private FileUploadRes uploadAvatar(MultipartFile avatar, User user) {
        try {
            if (avatar == null) {
                return null;
            }
            if (StringUtils.isNotBlank(user.getAvatarPath())) {
                delete(user.getAvatarPath());
            }
            List<String> names = Utils.convertStringToList(avatar.getOriginalFilename(), "\\.");
            if (Utils.isNullOrEmpty(names)) {
                throw new FormValidationException(language.getString("valid.user.upload_avatar.error"));
            }
            String filePath = String.format(language.getString("dropbox.file_path.avatar"),
                    user.getId(), Utils.getNow().getTime(), names.get(names.size() - 1));
            return upload(avatar, filePath);
        } catch (Exception e) {
            throw new FormValidationException(language.getString("valid.user.upload_avatar.error"));
        }
    }

    private void mapUpdatingReqToModel(UpdatingUserReq req, User user, FileUploadRes avatar) {
        user.setName(req.getName());
        if (!user.getEmail().equalsIgnoreCase(req.getEmail())) {
            user.setEmail(req.getEmail());
        }
        user.setColor(req.getColor());
        if (avatar != null) {
            user.setAvatar(avatar.getPreviewUrl());
            user.setAvatarPath(avatar.getFilePath());
        }
    }

    private void validateUpdatingReq(UpdatingUserReq req, User user) {
        if (req.getId() != user.getId()) {
            throw new ForbiddenException();
        }
        req.normalize();
        if (!user.getEmail().equalsIgnoreCase(req.getEmail()) && userRepository.existsByEmail(req.getEmail())) {
            throw new FormValidationException(language.getString("valid.user.email.is_existed"));
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
                user.getEmail(),
                user.getColor(),
                user.getAvatar());
    }

    private void validateChangingPasswordReq(ChangingPasswordReq req, User user) {
        if (StringUtils.isBlank(req.getOldPassword()) ||
                !passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new FormValidationException(language.getString("valid.user.change_password.old_password_not_match"));
        }
    }

    public ListObjectRes<UserWithNameItemRes> getUserList(FindUserReq req, Long userId) {
        validateUser(userId);

        req.normalize();
        Optional<Role> adminRoleOpt = roleRepository.findByName(UserRoleEnum.ROLE_ADMIN);
        List<User> users = userDao.findFriends(userId, adminRoleOpt.orElse(null), req);

        ListObjectRes<UserWithNameItemRes> res = new ListObjectRes<>();
        if (!Utils.isNullOrEmpty(users)) {
            List<Relationship> relationships = relationshipRepository.findByIdReqUserIdOrIdRecUserId(userId, userId);
            res.setResponses(users.stream()
                .map(u -> UserWithNameItemRes.by(u, relationships))
                .collect(Collectors.toList()));
        }
        return res;
    }
}
