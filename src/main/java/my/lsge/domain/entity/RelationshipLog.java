package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.RelationShipStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "relationship_logs")
public class RelationshipLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_user_id")
    private User reqUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rec_user_id")
    private User recUser;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private RelationShipStatusEnum status;

    public RelationshipLog() {

    }

    public RelationshipLog(Long id, User reqUser, User recUser, RelationShipStatusEnum status) {
        this.id = id;
        this.reqUser = reqUser;
        this.recUser = recUser;
        this.status = status;
    }
}
