package startspring.repository;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startspring.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(@NotEmpty String id);

    Optional<User> findByUsername(@NotEmpty String username);

    boolean existsByUserId(String id);
}
