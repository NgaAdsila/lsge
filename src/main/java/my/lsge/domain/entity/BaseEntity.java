package my.lsge.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class BaseEntity {
    @Id
    private int id;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    private int createdBy;

    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;

    @JsonProperty("modified_by")
    private int modifiedBy;
}
