package startspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startspring.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
