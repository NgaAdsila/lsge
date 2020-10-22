package my.lsge.domain.repository;

import my.lsge.domain.entity.Relationship;
import my.lsge.domain.entity.RelationshipId;
import my.lsge.domain.enums.RelationShipStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {

    List<Relationship> findByIdReqUserIdOrIdRecUserId(Long reqUserId, Long recUserId);

    @Query("select r from Relationship r where r.status = :status and (r.id.reqUserId = :userId or r.id.recUserId = :userId)")
    List<Relationship> findFriends(@Param("status") RelationShipStatusEnum status,
                                   @Param("userId") Long userId);
}
