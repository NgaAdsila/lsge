package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.relation.AddingFriendReq;
import my.lsge.application.dto.relation.FriendItemRes;
import my.lsge.application.dto.relation.UpdatingFriendStatusReq;
import my.lsge.application.exception.FormValidationException;
import my.lsge.domain.dao.RelationshipDao;
import my.lsge.domain.entity.Relationship;
import my.lsge.domain.entity.RelationshipId;
import my.lsge.domain.entity.RelationshipLog;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.RelationShipStatusEnum;
import my.lsge.domain.repository.RelationshipLogRepository;
import my.lsge.domain.repository.RelationshipRepository;
import my.lsge.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public ListObjectRes<FriendItemRes> getFriendList(Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        List<Relationship> relationships = relationshipRepository.findByIdReqUserIdOrIdRecUserId(userId, userId);

        ListObjectRes<FriendItemRes> res = new ListObjectRes<>();
        if (!Utils.isNullOrEmpty(relationships)) {
            res.setResponses(relationships.stream()
                    .map(r -> r.getId().getReqUserId().equals(userId) ? r.getRecUser() : r.getReqUser())
                    .map(FriendItemRes::by)
                    .collect(Collectors.toList()));
        }
        return res;
    }

    public void addFriend(AddingFriendReq req, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        if (userId.equals(req.getRecUserId())) {
            throw new FormValidationException(language.getString("relationship.add_self"));
        }
        User recUser = userRepository.getOne(req.getRecUserId());
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
