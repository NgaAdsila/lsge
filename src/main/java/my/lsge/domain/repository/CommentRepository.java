package my.lsge.domain.repository;

import my.lsge.domain.entity.Comment;
import my.lsge.domain.enums.CommentReferenceTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReferenceTypeAndReferenceIdIn(CommentReferenceTypeEnum referenceType, List<Long> referenceIds);

    List<Comment> findByReferenceTypeAndReferenceId(CommentReferenceTypeEnum referenceType, Long referenceId);

    List<Comment> findByParentId(long parentId);
}
