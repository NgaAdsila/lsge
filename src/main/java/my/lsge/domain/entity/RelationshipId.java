package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class RelationshipId implements Serializable {

    @Column(name = "req_user_id")
    private Long reqUserId;

    @Column(name = "rec_user_id")
    private Long recUserId;

    public RelationshipId() {

    }

    public RelationshipId(Long reqUserId, Long recUserId) {
        this.reqUserId = reqUserId;
        this.recUserId = recUserId;
    }

    public int hashCode() {
        return (int)(reqUserId + recUserId);
    }

    public boolean equals(Object object) {
        if (object instanceof RelationshipId) {
            RelationshipId otherId = (RelationshipId) object;
            return (otherId.reqUserId.equals(this.reqUserId) && otherId.recUserId.equals(this.recUserId))
                || (otherId.recUserId.equals(this.reqUserId) && otherId.reqUserId.equals(this.recUserId));
        }
        return false;
    }
}
