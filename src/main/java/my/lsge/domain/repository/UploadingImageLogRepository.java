package my.lsge.domain.repository;

import my.lsge.domain.entity.UploadingImageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadingImageLogRepository extends JpaRepository<UploadingImageLog, Long> {
}
