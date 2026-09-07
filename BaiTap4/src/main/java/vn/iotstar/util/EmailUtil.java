package vn.iotstar.util;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    private static final String FROM_EMAIL = "lyhiahan02@gmail.com";
    // TODO: Thay bằng App Password của Gmail (vào Google Account > Security > App Passwords)
    private static final String APP_PASSWORD = "ptcikcsaupfamjyy";

    /**
     * Tạo mã OTP ngẫu nhiên 6 chữ số
     */
    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Gửi email chứa mã OTP
     */
    public static boolean sendOtpEmail(String toEmail, String otp, String subject) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject, "UTF-8");

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 30px; "
                    + "border: 1px solid #e2e8f0; border-radius: 12px; background: #ffffff;'>"
                    + "<h2 style='color: #4f46e5; text-align: center; margin-bottom: 20px;'>ShoppingAdmin</h2>"
                    + "<p style='color: #475569; font-size: 15px; text-align: center;'>" + subject + "</p>"
                    + "<div style='background: linear-gradient(90deg, #4f46e5 0%, #7c3aed 100%); border-radius: 10px; "
                    + "padding: 20px; text-align: center; margin: 20px 0;'>"
                    + "<span style='font-size: 32px; font-weight: 700; color: #ffffff; letter-spacing: 8px;'>"
                    + otp + "</span></div>"
                    + "<p style='color: #94a3b8; font-size: 13px; text-align: center;'>"
                    + "Mã OTP có hiệu lực trong <b>2 phút</b>. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                    + "</div>";

            message.setContent(htmlContent, "text/html; charset=UTF-8");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
