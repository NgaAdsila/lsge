package my.lsge.domain.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class User extends BaseEntity {

    private String name;

    private String password;
}
