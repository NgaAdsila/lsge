package my.lsge.domain.entity;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @NaturalId(mutable = true)
    @Size(max = 40)
    @Email
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    //.............................TODO: infinite user........................................
    @OneToMany
    @JoinColumn(name = "i_user_id")
    public Set<Relationship> iRelationships;

    @OneToMany
    @JoinColumn(name = "y_user_id")
    public Set<Relationship> yRelationships;

    public Set<Relationship> relationships() {
        return Stream.concat(iRelationships.stream(), yRelationships.stream())
                .collect(Collectors.toSet());
    }

    public List<User> friendList() {
        return this.relationships().stream()
                .map(a -> a.getIUser().getId().equals(this.id) ? a.getYUser() : a.getIUser())
                .collect(Collectors.toList());
    }

    public User() {

    }

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
