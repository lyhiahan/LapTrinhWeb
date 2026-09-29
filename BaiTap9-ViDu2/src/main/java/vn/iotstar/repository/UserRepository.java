package vn.iotstar.repository;

import vn.iotstar.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    /**
     * Kiểm tra xung đột chéo giữa username và email:
     * - Một user đăng ký username trùng với email của người khác.
     * - Một user đăng ký email trùng với username của người khác.
     */
    default boolean existsByUsernameOrEmailConflict(String username, String email) {
        return existsByUsername(username)
            || existsByEmail(email)
            || existsByEmail(username)
            || existsByUsername(email);
    }
}
