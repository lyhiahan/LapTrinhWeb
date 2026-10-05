package vn.hcmute.util;

import java.util.Properties;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import vn.hcmute.service.IEmailService_24133016;

@Component
public class EmailUtil_24133016 implements IEmailService_24133016 {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil_24133016.class);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String fromEmail;
    private final boolean authEnabled;
    private final boolean startTlsEnabled;

    public EmailUtil_24133016(
            @Value("${app.mail.host:smtp.gmail.com}") String host,
            @Value("${app.mail.port:587}") int port,
            @Value("${app.mail.username:}") String username,
            @Value("${app.mail.password:}") String password,
            @Value("${app.mail.from:${app.mail.username:}}") String fromEmail,
            @Value("${app.mail.auth:true}") boolean authEnabled,
            @Value("${app.mail.starttls:true}") boolean startTlsEnabled) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.fromEmail = fromEmail;
        this.authEnabled = authEnabled;
        this.startTlsEnabled = startTlsEnabled;
    }

    // SecureRandom is suitable for OTPs; java.util.Random is predictable.
    @Override
    public String generateOtp() {
        int otp = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public boolean sendOtpEmail(String toEmail, String otp, String subject) {
        if (isBlank(host) || isBlank(fromEmail) || (authEnabled && (isBlank(username) || isBlank(password)))) {
            LOGGER.error("Không thể gửi OTP: cấu hình SMTP chưa đầy đủ");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", String.valueOf(authEnabled));
        props.put("mail.smtp.starttls.enable", String.valueOf(startTlsEnabled));
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = authEnabled
                ? Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                })
                : Session.getInstance(props);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, true));
            message.setSubject(subject, "UTF-8");
            message.setContent(
                "<html><body>" +
                "<h2>Mã xác thực OTP</h2>" +
                "<p>Mã OTP của bạn là: <b style='font-size:24px;color:#2196F3;'>" + otp + "</b></p>" +
                "<p>Mã này có hiệu lực trong 5 phút.</p>" +
                "<p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>" +
                "<hr><p><i>WebProject_24133016 - MSSV: 24133016</i></p>" +
                "</body></html>",
                "text/html; charset=UTF-8"
            );
            Transport.send(message);
            LOGGER.info("Đã gửi email OTP thành công");
            return true;
        } catch (Exception e) {
            LOGGER.error("Gửi email OTP thất bại: {}", e.getMessage());
            return false;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
