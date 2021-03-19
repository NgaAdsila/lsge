package my.lsge.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReactiveUserId implements Serializable {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    public int hashCode() {
        return Integer.parseInt(String.format("%d.%d", postId, userId));
    }

    public boolean equals(Object object) {
        if (object instanceof PostReactiveUserId) {
            PostReactiveUserId otherId = (PostReactiveUserId) object;
            return otherId.postId.equals(this.postId) && otherId.userId.equals(this.userId);
        }
        return false;
    }
}
