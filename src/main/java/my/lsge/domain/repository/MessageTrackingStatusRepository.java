package my.lsge.domain.repository;

import my.lsge.domain.entity.MessageTrackingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageTrackingStatusRepository extends JpaRepository<MessageTrackingStatus, Long> {
    List<MessageTrackingStatus> findByChatroomIdAndUserIdAndMessageIdLessThanEqualAndIsSeen(
            Long chatroomId, Long userId, Long messageId, boolean isSeen);

    List<MessageTrackingStatus> findByChatroomIdAndUserIdAndIsSeen(Long chatroomId, Long userId, boolean isSeen);
}
