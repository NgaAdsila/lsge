package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "uploading_image_logs")
public class UploadingImageLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String data;

    public UploadingImageLog() {

    }

    public UploadingImageLog(Long id, Long userId, String data) {
        this.id = id;
        this.userId = userId;
        this.data = data;
    }
}
