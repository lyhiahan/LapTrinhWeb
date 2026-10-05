package vn.hcmute.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hcmute.entity.User_24133016;
import vn.hcmute.repository.UserRepository_24133016;
import vn.hcmute.service.IUserService_24133016;
import vn.hcmute.service.IEmailService_24133016;
import vn.hcmute.util.PasswordUtil_24133016;

@Service
@Transactional
public class UserServiceImpl_24133016 implements IUserService_24133016 {

    private static final Duration OTP_VALIDITY = Duration.ofMinutes(5);
    private static final Duration OTP_RESEND_COOLDOWN = Duration.ofSeconds(60);

    private final UserRepository_24133016 userRepository;
    private final IEmailService_24133016 emailUtil;

    public UserServiceImpl_24133016(UserRepository_24133016 userRepository,
                                   IEmailService_24133016 emailUtil) {
        this.userRepository = userRepository;
        this.emailUtil = emailUtil;
    }

    @Override
    public User_24133016 login(String username, String password) {
        User_24133016 user = this.findByUsername(username);
        if (!matchesPassword(user, password)
                || user.getActive() == null || !user.getActive()) {
            return null;
        }

        // Transparently migrate old plaintext records after a successful login.
        if (!PasswordUtil_24133016.isHashed(user.getPassword())) {
            user.setPassword(PasswordUtil_24133016.hash(password));
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public boolean matchesPassword(User_24133016 user, String rawPassword) {
        return user != null && PasswordUtil_24133016.matches(rawPassword, user.getPassword());
    }

    @Override
    public User_24133016 findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User_24133016 findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (isBlank(username) || isBlank(password) || isBlank(email) || isBlank(fullname)) {
            return false;
        }
        if (username.length() > 50 || password.length() > 128 || email.length() > 150
                || fullname.length() > 50 || (phone != null && phone.length() > 15)) {
            return false;
        }

        Optional<User_24133016> optUser = userRepository.findByUsername(username);
        if (optUser.isPresent()) {
            User_24133016 existingUser = optUser.get();
            // Không cho một lần đăng ký mới thay email/mật khẩu của tài khoản
            // đang chờ OTP. Chỉ chủ tài khoản biết đúng mật khẩu và email cũ
            // mới được yêu cầu gửi lại OTP.
            if (Boolean.TRUE.equals(existingUser.getActive())
                    || !sameEmail(existingUser.getEmail(), email)
                    || !matchesPassword(existingUser, password)) {
                return false;
            }
            return issueOtp(existingUser, existingUser.getEmail(), true);
        }

        Optional<User_24133016> optEmail = userRepository.findByEmail(email);
        // Email đã thuộc bất kỳ tài khoản nào đều không được tái sử dụng.
        if (optEmail.isPresent()) {
            return false;
        }

        String otp = emailUtil.generateOtp();
        if (!emailUtil.sendOtpEmail(email, otp, "Mã xác thực OTP - WebProject_24133016")) {
            return false;
        }

        User_24133016 user = new User_24133016();
        user.setUsername(username);
        user.setPassword(PasswordUtil_24133016.hash(password));
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setAdmin(false);
        user.setActive(false);
        user.setOtp(otp);
        user.setOtpExpiry(Date.from(Instant.now().plus(OTP_VALIDITY)));
        // Flush tại đây để unique constraint được kiểm tra trước khi trả kết quả
        // về controller, tránh lỗi muộn ở lúc commit khi có hai đăng ký đồng thời.
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public boolean sendOtp(String email) {
        Optional<User_24133016> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return false;
        }

        User_24133016 user = opt.get();
        if (Boolean.TRUE.equals(user.getActive())) {
            return false;
        }
        return issueOtp(user, user.getEmail(), true);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        Instant now = Instant.now();
        Optional<User_24133016> opt = userRepository.findByEmail(email);
        if (opt.isEmpty() || opt.get().getOtp() == null) {
            return false;
        }

        User_24133016 user = opt.get();
        if (otp != null && PasswordUtil_24133016.matches(otp, user.getOtp())
                && user.getOtpExpiry() != null && user.getOtpExpiry().toInstant().isAfter(now)) {
            user.setActive(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean sameEmail(String first, String second) {
        return first != null && second != null && first.equalsIgnoreCase(second);
    }

    private boolean issueOtp(User_24133016 user, String destination, boolean enforceCooldown) {
        Instant now = Instant.now();
        if (isBlank(destination) || (enforceCooldown && isResendCooldownActive(user, now))) {
            return false;
        }

        String otp = emailUtil.generateOtp();
        if (!emailUtil.sendOtpEmail(destination, otp, "Mã xác thực OTP - WebProject_24133016")) {
            return false;
        }

        user.setOtp(otp);
        user.setOtpExpiry(Date.from(now.plus(OTP_VALIDITY)));
        userRepository.save(user);
        return true;
    }

    private boolean isResendCooldownActive(User_24133016 user, Instant now) {
        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            return false;
        }
        Instant sentAt = user.getOtpExpiry().toInstant().minus(OTP_VALIDITY);
        return sentAt.plus(OTP_RESEND_COOLDOWN).isAfter(now);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
