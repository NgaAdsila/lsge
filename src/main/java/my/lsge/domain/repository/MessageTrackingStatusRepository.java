package my.lsge.domain.repository;

import my.lsge.domain.entity.MessageTrackingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTrackingStatusRepository extends JpaRepository<MessageTrackingStatus, Long> {
}
