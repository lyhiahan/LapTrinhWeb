package vn.iotstar.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import vn.iotstar.entity.OtpToken;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(String email, String type);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<OtpToken> findFirstByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(String email, String type);

    void deleteByEmailAndType(String email, String type);
    void deleteByEmail(String email);
    Optional<OtpToken> findTopByEmailAndTypeOrderByCreatedAtDesc(String email, String type);
}
