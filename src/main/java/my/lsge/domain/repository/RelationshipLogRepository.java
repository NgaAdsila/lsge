package my.lsge.domain.repository;

import my.lsge.domain.entity.RelationshipLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipLogRepository extends JpaRepository<RelationshipLog, Long> {

}
