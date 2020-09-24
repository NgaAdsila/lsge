package my.lsge.domain.dao;

import my.lsge.domain.entity.Relationship;

public interface RelationshipDao {
    Relationship getOne(Long firstUserId, Long secondUserId);
}
