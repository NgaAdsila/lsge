package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.CommentReferenceTypeEnum;
import my.lsge.domain.enums.CommentStatusEnum;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Size(max = 16)
    private CommentReferenceTypeEnum referenceType;

    private long referenceId;

    @NotBlank
    private String message;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Nullable
    private Comment commentParent;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Size(max = 16)
    private CommentStatusEnum status;

    public Comment() {
        this.commentParent = null;
        this.status = CommentStatusEnum.CREATED;
    }

    public Comment(Long id, CommentReferenceTypeEnum referenceType, long referenceId,
                   String message, Comment commentParent, CommentStatusEnum status) {
        this.id = id;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.message = message;
        this.commentParent = commentParent;
        this.status = status;
    }
}
