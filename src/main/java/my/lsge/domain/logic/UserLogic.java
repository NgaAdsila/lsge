package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.user.*;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.FormValidationException;
import my.lsge.application.service.ImageService;
import my.lsge.domain.dao.UserDao;
import my.lsge.domain.entity.Relationship;
import my.lsge.domain.entity.Role;
import my.lsge.domain.entity.UploadingImageLog;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.UserRoleEnum;
import my.lsge.domain.repository.RelationshipRepository;
import my.lsge.domain.repository.RoleRepository;
import my.lsge.domain.repository.UploadingImageLogRepository;
import my.lsge.util.JsonUtils;
import my.lsge.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserLogic extends BaseLogic {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UploadingImageLogRepository uploadingImageLogRepository;

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
        UploadImageRes imageRes = uploadAvatarToStorage(req.getAvatar(), userId);
        mapUpdatingReqToModel(req, user, imageRes);
        userRepository.saveAndFlush(user);
        return new UserSummary(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getColor(),
                user.getAvatar());
    }

    private UploadImageRes uploadAvatarToStorage(String avatar, Long userId) throws FormValidationException {
        try {
            if (StringUtils.isBlank(avatar)) {
                return null;
            }
            UploadImageRes res = imageService.upload(avatar);
            if (res != null && res.getData() != null) {
                UploadingImageLog log = new UploadingImageLog(0L, userId, JsonUtils.toJson(res.getData()));
                uploadingImageLogRepository.save(log);
            }
            return res;
        } catch (Exception e) {
            throw new FormValidationException(language.getString("valid.user.upload_avatar.error"));
        }
    }

    private void mapUpdatingReqToModel(UpdatingUserReq req, User user, UploadImageRes imageRes) {
        user.setName(req.getName());
        if (!user.getEmail().equalsIgnoreCase(req.getEmail())) {
            user.setEmail(req.getEmail());
        }
        user.setColor(req.getColor());
        if (imageRes != null && imageRes.getData() != null && imageRes.getData().getThumb() != null) {
            user.setAvatar(imageRes.getData().getThumb().getUrl());
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
