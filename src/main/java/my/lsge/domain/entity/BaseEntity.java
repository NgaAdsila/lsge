package my.lsge.domain.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Slf4j(topic = "AUDIT")
public abstract class BaseEntity implements Serializable {

    @Version
    private Integer version;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private int createdBy;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @LastModifiedBy
    private int modifiedBy;

    private boolean isDeleted = false;

    @PostPersist
    public void onPostPersist() {

    }

    @PostUpdate
    public void onPostUpdate() {
    }

    @PostRemove
    public void onPostRemove() {
    }

    @PostLoad
    public void onPostLoad() {
    }
}
