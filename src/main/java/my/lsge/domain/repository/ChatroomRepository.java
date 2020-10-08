package my.lsge.domain.repository;

import my.lsge.domain.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

}
