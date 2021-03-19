package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.RelationShipStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "relationships")
public class Relationship extends BaseEntity {

    @EmbeddedId
    private RelationshipId id;

    @MapsId(value = "reqUserId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_user_id")
    private User reqUser;

    @MapsId(value = "recUserId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rec_user_id")
    private User recUser;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private RelationShipStatusEnum status;

    public Relationship() {
        this.status = RelationShipStatusEnum.PENDING;
    }

    public Relationship(RelationshipId id, RelationShipStatusEnum status) {
        this.id = id;
        this.status = status;
    }
}
