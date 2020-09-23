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

    @MapsId(value = "iUserId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "i_user_id")
    private User iUser;

    @MapsId(value = "yUserId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "y_user_id")
    private User yUser;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private RelationShipStatusEnum status;

    public Relationship() {
        this.status = RelationShipStatusEnum.PENDING;
    }

    public Relationship(User iUser, User yUser, RelationShipStatusEnum status) {
        this.iUser = iUser;
        this.yUser = yUser;
        this.status = status;
    }
}
