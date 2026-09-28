package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.iotstar.service.EmailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendOtp(String email, String otp, String subject) {
        log.info("==================================================");
        log.info(">>> MA OTP XAC THUC CHO [{}]: {}", email, otp);
        log.info("==================================================");
        java.util.concurrent.CompletableFuture.runAsync(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject(subject);
                message.setText("""
                    Xin chao,
                    Ma OTP cua ban la: %s
                    OTP co hieu luc trong 5 phut va chi su dung mot lan.
                    """.formatted(otp));
                mailSender.send(message);
            } catch (Exception e) {
                log.warn("Khong the gui mail qua SMTP cho {}: {}", email, e.getMessage());
            }
        });
    }
}
