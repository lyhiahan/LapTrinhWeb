package vn.hcmute.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hcmute.entity.User_24133016;
import vn.hcmute.repository.UserRepository_24133016;
import vn.hcmute.service.IUserService_24133016;
import vn.hcmute.util.EmailUtil_24133016;

@Service
@Transactional
public class UserServiceImpl_24133016 implements IUserService_24133016 {

    @Autowired
    private UserRepository_24133016 userRepository;

    @Override
    public User_24133016 login(String username, String password) {
        User_24133016 user = this.findByUsername(username);
        if (user != null && password != null && password.equals(user.getPassword())) {
            if (user.getActive() == null || !user.getActive()) {
                return null;
            }
            return user;
        }
        return null;
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
        Optional<User_24133016> optUser = userRepository.findByUsername(username);
        if (optUser.isPresent() && Boolean.TRUE.equals(optUser.get().getActive())) {
            return false;
        }
        Optional<User_24133016> optEmail = userRepository.findByEmail(email);
        if (optEmail.isPresent() && Boolean.TRUE.equals(optEmail.get().getActive()) && !optEmail.get().getUsername().equals(username)) {
            return false;
        }

        User_24133016 user = optUser.orElse(optEmail.orElse(new User_24133016()));
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setAdmin(false);
        user.setActive(false);
        userRepository.save(user);

        sendOtp(email);
        return true;
    }

    @Override
    public boolean sendOtp(String email) {
        Optional<User_24133016> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return false;
        }

        User_24133016 user = opt.get();
        String otp = EmailUtil_24133016.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(new java.util.Date(System.currentTimeMillis() + 5 * 60 * 1000)); // 5 phút
        userRepository.save(user);

        System.out.println("=================================================");
        System.out.println(">>> [OTP SYSTEM] Email: " + email + " | MÃ OTP: " + otp);
        System.out.println("=================================================");

        return EmailUtil_24133016.sendOtpEmail(email, otp, "Mã xác thực OTP - WebProject_24133016");
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        Optional<User_24133016> opt = userRepository.findByEmail(email);
        if (opt.isEmpty() || opt.get().getOtp() == null) {
            return false;
        }

        User_24133016 user = opt.get();
        if (otp.equals(user.getOtp()) && user.getOtpExpiry() != null
                && user.getOtpExpiry().after(new java.util.Date())) {
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
}
