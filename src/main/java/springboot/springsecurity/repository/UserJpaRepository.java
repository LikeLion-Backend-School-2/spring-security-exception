package springboot.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.springsecurity.domain.entity.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
