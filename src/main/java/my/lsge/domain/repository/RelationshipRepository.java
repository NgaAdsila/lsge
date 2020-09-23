package my.lsge.domain.repository;

import my.lsge.domain.entity.Relationship;
import my.lsge.domain.entity.RelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {

}
