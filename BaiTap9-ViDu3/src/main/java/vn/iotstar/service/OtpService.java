package vn.iotstar.service;

public interface OtpService {
    void sendRegisterOtp(String email);
    void sendRegisterOtp(String email, String clientIp);
    boolean verifyRegisterOtp(String email, String otp);
    void sendResetPasswordOtp(String email);
    void sendResetPasswordOtp(String email, String clientIp);
    boolean verifyResetPasswordOtp(String email, String otp);
}
