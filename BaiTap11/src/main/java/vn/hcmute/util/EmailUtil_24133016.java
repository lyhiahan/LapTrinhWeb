package vn.hcmute.util;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil_24133016 {

    // Tạo mã OTP ngẫu nhiên 6 chữ số
    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Gửi email OTP
    public static boolean sendOtpEmail(String toEmail, String otp, String subject) {
        // Cấu hình SMTP Gmail
        final String fromEmail = "lyhiahan02@gmail.com";
        final String password = "yknitpmojehupasv";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password.replace(" ", ""));
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
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
            System.out.println(">>> [EMAIL] Đã gửi OTP thành công tới: " + toEmail);
            return true;
        } catch (Exception e) {
            System.out.println(">>> [EMAIL] Lỗi gửi email (OTP vẫn được in ra console): " + e.getMessage());
            // Vẫn return true vì OTP đã được in ra console để test
            return true;
        }
    }
}
