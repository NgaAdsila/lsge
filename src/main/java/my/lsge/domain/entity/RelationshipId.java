package my.lsge.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RelationshipId implements Serializable {

    @Column(name = "i_user_id")
    private long iUserId;

    @Column(name = "y_user_id")
    private long yUserId;

    public int hashCode() {
        return (int)(iUserId + yUserId);
    }

    public boolean equals(Object object) {
        if (object instanceof RelationshipId) {
            RelationshipId otherId = (RelationshipId) object;
            return (otherId.iUserId == this.iUserId) && (otherId.yUserId == this.yUserId);
        }
        return false;
    }
}
