package my.lsge.domain.repository;

import my.lsge.domain.entity.Role;
import my.lsge.domain.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRoleEnum name);
}
