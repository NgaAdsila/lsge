package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.UserRoleEnum;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private UserRoleEnum name;

    public Role() {

    }

    public Role(UserRoleEnum name) {
        this.name = name;
    }
}
