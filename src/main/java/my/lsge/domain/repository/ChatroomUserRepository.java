package my.lsge.domain.repository;

import my.lsge.domain.entity.ChatroomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomUserRepository extends JpaRepository<ChatroomUser, Long> {

    List<ChatroomUser> findAllByChatroomId(Long chatroomId);
}
