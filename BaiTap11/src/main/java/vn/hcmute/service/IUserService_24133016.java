package vn.hcmute.service;

import vn.hcmute.entity.User_24133016;

public interface IUserService_24133016 {
    User_24133016 login(String username, String password);
    boolean matchesPassword(User_24133016 user, String rawPassword);
    User_24133016 findByUsername(String username);
    User_24133016 findByEmail(String email);
    boolean register(String username, String password, String email, String fullname, String phone);
    boolean sendOtp(String email);
    boolean verifyOtp(String email, String otp);
    boolean checkExistUsername(String username);
    boolean checkExistEmail(String email);
}
