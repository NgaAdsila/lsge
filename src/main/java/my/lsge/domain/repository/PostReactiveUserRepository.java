package my.lsge.domain.repository;

import my.lsge.domain.entity.PostReactiveUser;
import my.lsge.domain.entity.PostReactiveUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactiveUserRepository extends JpaRepository<PostReactiveUser, PostReactiveUserId> {

}
