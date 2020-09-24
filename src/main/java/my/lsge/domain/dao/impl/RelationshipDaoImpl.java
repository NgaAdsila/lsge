package my.lsge.domain.dao.impl;

import my.lsge.domain.dao.RelationshipDao;
import my.lsge.domain.entity.Relationship;
import org.springframework.stereotype.Repository;

@Repository
public class RelationshipDaoImpl extends BaseDaoImpl<Relationship> implements RelationshipDao {
    @Override
    public Relationship getOne(Long firstUserId, Long secondUserId) {
        if (firstUserId == null || secondUserId == null) {
            return  null;
        }
        String query = String.format("SELECT rs FROM Relationship rs " +
                        "WHERE (rs.id.reqUserId = %s AND rs.id.recUserId = %s) " +
                        "OR (rs.id.reqUserId = %s AND rs.id.recUserId = %s)",
                firstUserId, secondUserId, secondUserId, firstUserId);
        return entityManager.createQuery(query, Relationship.class).getSingleResult();
    }
}
