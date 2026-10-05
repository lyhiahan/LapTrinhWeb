package vn.hcmute.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hcmute.entity.User_24133016;

@Repository
public interface UserRepository_24133016 extends JpaRepository<User_24133016, String> {
    Optional<User_24133016> findByUsername(String username);
    Optional<User_24133016> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
