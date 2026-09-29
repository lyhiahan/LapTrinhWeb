package vn.iotstar.service;

import vn.iotstar.dto.RegisterDTO;

public interface AuthService {
    void register(RegisterDTO dto);
    void register(RegisterDTO dto, String clientIp);
    boolean verifyRegister(String email, String otp);
    void forgotPassword(String email);
    void forgotPassword(String email, String clientIp);
    void resetPasswordWithOtp(String email, String otp, String newPassword);
}
