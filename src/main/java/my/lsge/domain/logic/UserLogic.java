package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.OptionRes;
import my.lsge.application.dto.admin.user.*;
import my.lsge.application.dto.user.*;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.FormValidationException;
import my.lsge.application.exception.NotFoundException;
import my.lsge.domain.dao.UserDao;
import my.lsge.domain.entity.BaseEntity;
import my.lsge.domain.entity.Relationship;
import my.lsge.domain.entity.Role;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.UserRoleEnum;
import my.lsge.domain.enums.UserStatusEnum;
import my.lsge.domain.repository.RelationshipRepository;
import my.lsge.domain.repository.RoleRepository;
import my.lsge.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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

    public UserFilterRes filter(UserFilterReq req, Long userId) {
        validateUser(userId, UserRoleEnum.ROLE_ADMIN);

        req.normalize();
        UserFilterRes res = new UserFilterRes();
        long count = userDao.filterCount(req, userId);
        if (count > 0) {
            res.setCount(count);
            List<User> users = userDao.filter(req, userId);
            if (!Utils.isNullOrEmpty(users)) {
                res.setResponses(users.stream().map(UserFilterItemRes::by).collect(Collectors.toList()));
            }
        }
        res.setPaging(req);
        return res;
    }

    public ListObjectRes<OptionRes> getRoleOptions(Long userId) {
        validateUser(userId, UserRoleEnum.ROLE_ADMIN);

        ListObjectRes<OptionRes> res = new ListObjectRes<>();
        List<Role> roles = roleRepository.findAll();
        if (!Utils.isNullOrEmpty(roles)) {
            res.setResponses(roles.stream()
                    .map(r -> new OptionRes(r.getId(), r.getName().getTitle()))
                    .sorted(Comparator.comparing(OptionRes::getText))
                    .collect(Collectors.toList()));
        }
        return res;
    }

    public Integer bandUsers(UpdatingUserListReq req, Long userId) {
        validateUser(userId, UserRoleEnum.ROLE_ADMIN);

        req.normalize();
        if (Utils.isNullOrEmpty(req.getUserIds())) {
            throw new NotFoundException(language.getString("manager.user.empty_list"));
        }
        if (req.getUserIds().contains(userId)) {
            throw new FormValidationException(language.getString("manager.user.not_band_yourself"));
        }
        List<User> users = userRepository.findAllByIdIn(req.getUserIds());
        if (Utils.isNullOrEmpty(users) || users.stream().allMatch(BaseEntity::isDeleted)) {
            throw new NotFoundException(language.getString("manager.user.not_found"));
        }

        List<User> updatingUsers = users.stream()
                .filter(u -> !u.isDeleted())
                .peek(u -> u.setDeleted(true))
                .collect(Collectors.toList());
        userRepository.saveAll(updatingUsers);
        return updatingUsers.size();
    }

    public Integer activeUsers(UpdatingUserListReq req, Long userId) {
        validateUser(userId, UserRoleEnum.ROLE_ADMIN);

        req.normalize();
        if (Utils.isNullOrEmpty(req.getUserIds())) {
            throw new NotFoundException(language.getString("manager.user.empty_list"));
        }
        if (req.getUserIds().contains(userId)) {
            throw new FormValidationException(language.getString("manager.user.not_active_yourself"));
        }
        List<User> users = userRepository.findAllByIdIn(req.getUserIds());
        if (Utils.isNullOrEmpty(users) || users.stream().noneMatch(BaseEntity::isDeleted)) {
            throw new NotFoundException(language.getString("manager.user.not_existed_or_active"));
        }

        List<User> updatingUsers = users.stream()
                .filter(BaseEntity::isDeleted)
                .peek(u -> u.setDeleted(false))
                .collect(Collectors.toList());
        userRepository.saveAll(updatingUsers);
        return updatingUsers.size();
    }

    public UpdatingUserPasswordRes resetPasswordUsers(UpdatingUserListReq req, Long userId) {
        validateUser(userId, UserRoleEnum.ROLE_ADMIN);

        req.normalize();
        if (Utils.isNullOrEmpty(req.getUserIds())) {
            throw new NotFoundException(language.getString("manager.user.empty_list"));
        }
        if (req.getUserIds().contains(userId)) {
            throw new FormValidationException(language.getString("manager.user.not_reset_password_yourself"));
        }
        List<User> users = userRepository.findAllByIdIn(req.getUserIds());
        if (Utils.isNullOrEmpty(users)) {
            throw new NotFoundException(language.getString("manager.user.not_existed"));
        }
        if (users.stream().anyMatch(BaseEntity::isDeleted)) {
            throw new FormValidationException(language.getString("manager.user.not_reset_password_for_banned_user"));
        }

        UpdatingUserPasswordRes res = new UpdatingUserPasswordRes();
        List<User> updatingUsers = users.stream()
                .filter(u -> !u.isDeleted())
                .peek(u -> {
                    String rawPassword = Utils.randomPassword();
                    u.setPassword(passwordEncoder.encode(rawPassword));
                    res.getUserList().add(new UserPasswordRes(u, rawPassword));
                })
                .collect(Collectors.toList());
        userRepository.saveAll(updatingUsers);
        return res;
    }

    public Integer updateRoleForUsers(UpdatingUserRoleReq req, Long userId) {
        validateUser(userId, UserRoleEnum.ROLE_ADMIN);

        req.normalize();
        if (Utils.isNullOrEmpty(req.getUserRoleList())) {
            throw new NotFoundException(language.getString("manager.user.empty_list"));
        }
        if (req.getUserRoleList().stream().anyMatch(ur -> userId.equals(ur.getId()))) {
            throw new FormValidationException(language.getString("manager.user.not_update_role_yourself"));
        }
        List<Long> userIds = req.getUserRoleList().stream().map(UserRoleReq::getId).collect(Collectors.toList());
        List<User> users = userRepository.findAllByIdIn(userIds);
        if (Utils.isNullOrEmpty(users)) {
            throw new NotFoundException(language.getString("manager.user.not_existed"));
        }
        if (users.stream().anyMatch(BaseEntity::isDeleted)) {
            throw new FormValidationException(language.getString("manager.user.not_update_role_for_banned_user"));
        }
        List<Role> roles = roleRepository.findAll();
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        if (req.getUserRoleList().stream()
                .anyMatch(ur -> Utils.isNullOrEmpty(ur.getRoles()) || containInvalidRole(roleIds, ur.getRoles()))) {
            throw new FormValidationException(language.getString("manager.user.invalid_role"));
        }
        List<User> updatingUsers = new ArrayList<>();
        req.getUserRoleList().forEach(ur -> {
            if (Utils.isNullOrEmpty(ur.getRoles()) || containInvalidRole(roleIds, ur.getRoles())) {
                return;
            }
            User user = users.stream().filter(u -> u.getId().equals(ur.getId())).findFirst().orElse(null);
            if (user == null || user.isDeleted()) {
                return;
            }
            Set<Role> userRoles = roles.stream().filter(r -> ur.getRoles().contains(r.getId()))
                    .collect(Collectors.toSet());
            Set<Role> removedRoles = user.getRoles().stream()
                    .filter(r -> !userRoles.contains(r))
                    .collect(Collectors.toSet());
            if (!removedRoles.isEmpty()) {
                removedRoles.forEach(user::removeRole);
            }
            user.setRoles(userRoles);
            updatingUsers.add(user);
        });
        userRepository.saveAll(updatingUsers);
        return updatingUsers.size();
    }

    private boolean containInvalidRole(List<Long> roleIds, List<Long> userRoleIds) {
        return Utils.isNullOrEmpty(roleIds) || userRoleIds.stream().anyMatch(r -> !roleIds.contains(r));
    }
}
