package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
