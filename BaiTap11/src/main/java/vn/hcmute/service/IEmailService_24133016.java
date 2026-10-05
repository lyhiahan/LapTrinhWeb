package vn.hcmute.service;

public interface IEmailService_24133016 {
    String generateOtp();
    boolean sendOtpEmail(String toEmail, String otp, String subject);
}
