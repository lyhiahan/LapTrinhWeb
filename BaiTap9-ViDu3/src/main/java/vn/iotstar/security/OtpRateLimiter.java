package vn.iotstar.security;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpRateLimiter {

    private static final long COOLDOWN_SECONDS = 60;
    private static final int MAX_REQUESTS_PER_EMAIL = 5;
    private static final int MAX_REQUESTS_PER_IP = 10;
    private static final Duration WINDOW_DURATION = Duration.ofMinutes(15);

    private final Map<String, Instant> emailLastSent = new ConcurrentHashMap<>();
    private final Map<String, Deque<Instant>> emailRequestHistory = new ConcurrentHashMap<>();
    private final Map<String, Deque<Instant>> ipRequestHistory = new ConcurrentHashMap<>();

    /**
     * Kiểm tra và ghi nhận lượt gửi OTP cho email và IP.
     * Ném IllegalStateException nếu vi phạm thời gian chờ 60s hoặc vượt hạn mức.
     */
    public synchronized void checkAndRecord(String email, String ip) {
        Instant now = Instant.now();
        String normalizedEmail = (email != null) ? email.trim().toLowerCase() : "";
        String normalizedIp = (ip != null && !ip.isBlank()) ? ip.trim() : "unknown";

        // 1. Kiểm tra Cooldown 60 giây giữa 2 lần gửi cho cùng một email
        if (!normalizedEmail.isBlank()) {
            Instant lastSent = emailLastSent.get(normalizedEmail);
            if (lastSent != null) {
                long elapsed = Duration.between(lastSent, now).getSeconds();
                if (elapsed < COOLDOWN_SECONDS) {
                    long waitSeconds = COOLDOWN_SECONDS - elapsed;
                    throw new IllegalStateException("Vui lòng đợi " + waitSeconds + " giây trước khi yêu cầu mã OTP tiếp theo.");
                }
            }
        }

        // 2. Kiểm tra giới hạn số lần gửi cho Email (tối đa 5 lần trong 15 phút)
        if (!normalizedEmail.isBlank()) {
            Deque<Instant> emailDeque = emailRequestHistory.computeIfAbsent(normalizedEmail, k -> new ArrayDeque<>());
            cleanExpired(emailDeque, now);
            if (emailDeque.size() >= MAX_REQUESTS_PER_EMAIL) {
                throw new IllegalStateException("Email này đã nhận quá nhiều mã OTP. Vui lòng thử lại sau 15 phút.");
            }
        }

        // 3. Kiểm tra giới hạn số lần gửi cho IP (tối đa 10 lần trong 15 phút)
        if (!normalizedIp.equalsIgnoreCase("unknown")) {
            Deque<Instant> ipDeque = ipRequestHistory.computeIfAbsent(normalizedIp, k -> new ArrayDeque<>());
            cleanExpired(ipDeque, now);
            if (ipDeque.size() >= MAX_REQUESTS_PER_IP) {
                throw new IllegalStateException("Địa chỉ IP của bạn đã gửi yêu cầu quá nhiều lần. Vui lòng thử lại sau 15 phút.");
            }
        }

        // 4. Ghi nhận thời gian gửi thành công
        if (!normalizedEmail.isBlank()) {
            emailLastSent.put(normalizedEmail, now);
            emailRequestHistory.get(normalizedEmail).addLast(now);
        }
        if (!normalizedIp.equalsIgnoreCase("unknown")) {
            ipRequestHistory.get(normalizedIp).addLast(now);
        }
    }

    private void cleanExpired(Deque<Instant> deque, Instant now) {
        Instant threshold = now.minus(WINDOW_DURATION);
        while (!deque.isEmpty() && deque.peekFirst().isBefore(threshold)) {
            deque.pollFirst();
        }
    }

    /**
     * Dọn dẹp bộ nhớ (gọi định kỳ hoặc khi cần reset)
     */
    public synchronized void resetFor(String email) {
        if (email != null) {
            String normalizedEmail = email.trim().toLowerCase();
            emailLastSent.remove(normalizedEmail);
            emailRequestHistory.remove(normalizedEmail);
        }
    }
}
