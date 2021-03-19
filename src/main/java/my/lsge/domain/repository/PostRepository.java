package my.lsge.domain.repository;

import my.lsge.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByIdIn(List<Long> ids);
}
