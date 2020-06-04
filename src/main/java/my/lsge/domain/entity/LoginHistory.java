package my.lsge.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "login_histories")
public class LoginHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    private String ipAddress;

    private String browser;

    public LoginHistory() {

    }

    public LoginHistory(User user, String ipAddress, String browser) {
        this.user = user;
        this.ipAddress = ipAddress;
        this.browser = browser;
    }
}
