package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.relation.AddingFriendReq;
import my.lsge.application.dto.relation.FriendItemRes;
import my.lsge.application.dto.relation.UpdatingFriendStatusReq;
import my.lsge.application.exception.FormValidationException;
import my.lsge.application.exception.NotAcceptableException;
import my.lsge.domain.dao.RelationshipDao;
import my.lsge.domain.entity.Relationship;
import my.lsge.domain.entity.RelationshipId;
import my.lsge.domain.entity.User;
import my.lsge.domain.enums.RelationShipStatusEnum;
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
        validateUser(userId);

        Relationship relationship = relationshipDao.getOne(userId, req.getRecUserId());
        if (relationship == null || relationship.isDeleted()) {
            relationship = new Relationship(new RelationshipId(userId, req.getRecUserId()), RelationShipStatusEnum.PENDING);
        } else {
            throw new FormValidationException(language.getString("relationship.is_existed"));
        }
        relationshipRepository.save(relationship);
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
    }

    public void cancel(UpdatingFriendStatusReq req, Long userId) {
        validateUser(userId);

        Relationship relationship = relationshipDao.getOne(userId, req.getUserId());
        if (relationship == null || relationship.isDeleted()) {
            throw new FormValidationException(language.getString("relationship.is_cancelled"));
        }
        relationship.setDeleted(true);
        relationshipRepository.save(relationship);
    }
}
