package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.chatroom.ChatroomWithLastMessageDTO;
import my.lsge.application.dto.relation.AddingFriendReq;
import my.lsge.application.dto.relation.FriendItemRes;
import my.lsge.application.dto.relation.FriendListRes;
import my.lsge.application.dto.relation.UpdatingFriendStatusReq;
import my.lsge.application.exception.FormValidationException;
import my.lsge.domain.dao.ChatroomDao;
import my.lsge.domain.dao.RelationshipDao;
import my.lsge.domain.entity.*;
import my.lsge.domain.enums.ChatroomTypeEnum;
import my.lsge.domain.enums.RelationShipStatusEnum;
import my.lsge.domain.repository.RelationshipLogRepository;
import my.lsge.domain.repository.RelationshipRepository;
import my.lsge.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RelationshipLogic extends BaseLogic {

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private RelationshipDao relationshipDao;

    @Autowired
    private RelationshipLogRepository relationshipLogRepository;

    @Autowired
    private ChatroomDao chatroomDao;

    public FriendListRes getFriendList(Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        List<Relationship> relationships = relationshipRepository.findByIdReqUserIdOrIdRecUserId(userId, userId);

        FriendListRes res = new FriendListRes();
        if (!Utils.isNullOrEmpty(relationships)) {
            Map<Long, Message> messageList = getLastMessageListByUser(userId);
            res.setFriends(relationships.stream()
                    .filter(r -> r.getStatus().equals(RelationShipStatusEnum.APPROVED))
                    .map(r -> r.getId().getReqUserId().equals(userId) ? r.getRecUser() : r.getReqUser())
                    .sorted(Comparator.comparing(User::getName))
                    .map(u -> FriendItemRes.by(u, messageList))
                    .collect(Collectors.toList()));
            res.setRequestedFriends(relationships.stream()
                    .filter(r -> r.getStatus().equals(RelationShipStatusEnum.PENDING)
                            && r.getId().getRecUserId().equals(userId))
                    .sorted(Comparator.comparing(Relationship::getCreatedAt))
                    .map(r -> FriendItemRes.by(r.getReqUser()))
                    .collect(Collectors.toList()));
        }
        return res;
    }

    private Map<Long, Message> getLastMessageListByUser(Long userId) {
        List<ChatroomWithLastMessageDTO> chatrooms = chatroomDao.findAllByTypeAndUser(ChatroomTypeEnum.NORMAL, userId);
        Map<Long, Message> res = new HashMap<>();
        if (!Utils.isNullOrEmpty(chatrooms)) {
            for (ChatroomWithLastMessageDTO chatroom : chatrooms) {
                res.put(chatroom.getUser().getId(), chatroom.getLastMessage());
            }
        }
        return res;
    }

    public void addFriend(AddingFriendReq req, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        if (userId.equals(req.getRecUserId())) {
            throw new FormValidationException(language.getString("relationship.add_self"));
        }
        User recUser = userRepository.findById(req.getRecUserId()).orElse(null);
        if (recUser == null || recUser.isDeleted()) {
            throw new FormValidationException(language.getString("valid.user.is_not_existed"));
        }
        Relationship relationship = relationshipDao.getOne(userId, req.getRecUserId());
        if (relationship == null || relationship.isDeleted()) {
            relationship = new Relationship();
            relationship.setId(new RelationshipId(userId, req.getRecUserId()));
            relationship.setReqUser(user);
            relationship.setRecUser(recUser);
        } else {
            throw new FormValidationException(language.getString("relationship.is_existed"));
        }
        relationshipRepository.save(relationship);
        saveLog(relationship, RelationShipStatusEnum.PENDING);
    }

    private void saveLog(Relationship relationship, RelationShipStatusEnum status) {
        RelationshipLog log = new RelationshipLog(0L, relationship.getReqUser(), relationship.getRecUser(), status);
        relationshipLogRepository.save(log);
    }

    public void approve(UpdatingFriendStatusReq req, Long userId) {
        validateUser(userId);

        Relationship relationship = relationshipDao.getOne(userId, req.getUserId());
        if (relationship == null || relationship.isDeleted()
                || !relationship.getStatus().equals(RelationShipStatusEnum.PENDING)) {
            throw new FormValidationException(language.getString("relationship.is_not_existed"));
        }
        relationship.setStatus(RelationShipStatusEnum.APPROVED);
        relationshipRepository.save(relationship);
        saveLog(relationship, RelationShipStatusEnum.APPROVED);
    }

    public void cancel(UpdatingFriendStatusReq req, Long userId) {
        validateUser(userId);

        Relationship relationship = relationshipDao.getOne(userId, req.getUserId());
        if (relationship == null || relationship.isDeleted()) {
            throw new FormValidationException(language.getString("relationship.is_cancelled"));
        }
        saveLog(relationship, RelationShipStatusEnum.CANCEL);
        relationshipRepository.delete(relationship);
    }
}
