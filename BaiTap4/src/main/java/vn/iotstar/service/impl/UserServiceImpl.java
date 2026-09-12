package vn.iotstar.service.impl;

import java.io.File;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.EmailUtil;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String username, String password) {
        User user = this.findByUsername(username);
        if (user != null && password != null && password.equals(user.getPassword())) {
            if (!user.getIsActive()) {
                return null;
            }
            return user;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (checkExistUsername(username)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        User user = new User(email, username, fullname, password, null, 5, phone, date);
        user.setIsActive(false);
        userRepository.save(user);

        sendOtp(email);
        return true;
    }

    @Override
    public boolean sendOtp(String email) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return false;
        }

        User user = opt.get();
        String otp = EmailUtil.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(new java.util.Date(System.currentTimeMillis() + 5 * 60 * 1000)); // Hạn 5 phút
        userRepository.save(user);

        System.out.println("=================================================");
        System.out.println(">>> [OTP SYSTEM] Email: " + email + " | MÃ OTP: " + otp);
        System.out.println("=================================================");

        return EmailUtil.sendOtpEmail(email, otp, "Mã xác thực OTP - ShoppingAdmin");
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty() || opt.get().getOtp() == null) {
            return false;
        }

        User user = opt.get();
        if (otp.equals(user.getOtp()) && user.getOtpExpiry() != null
                && user.getOtpExpiry().after(new java.util.Date())) {
            user.setIsActive(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            return false;
        }
        User user = opt.get();
        user.setPassword(newPassword);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public void insert(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateProfile(User user) {
        Optional<User> opt = userRepository.findByUserName(user.getUserName());
        if (opt.isPresent()) {
            User existingUser = opt.get();
            existingUser.setFullName(user.getFullName());
            existingUser.setPhone(user.getPhone());
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                existingUser.setAvatar(user.getAvatar());
            }
            userRepository.save(existingUser);
        }
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(int id) throws Exception {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            User user = opt.get();
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                File avatarFile = new File(Constant.DIR + "/" + user.getAvatar());
                if (avatarFile.exists()) {
                    avatarFile.delete();
                }
            }
            userRepository.deleteById(id);
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findPaginated(int page, int pageSize) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), pageSize);
        return userRepository.findAll(pageable).getContent();
    }

    @Override
    public List<User> searchPaginated(String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), pageSize);
        return userRepository.findByUserNameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, keyword, pageable).getContent();
    }

    @Override
    public Page<User> search(String keyword, Pageable pageable) {
        return userRepository.findByUserNameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, keyword, pageable);
    }

    @Override
    public int count() {
        return (int) userRepository.count();
    }

    @Override
    public int countSearch(String keyword) {
        return (int) userRepository.findByUserNameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, keyword, Pageable.unpaged()).getTotalElements();
    }

    @Override
    public void adminInsert(User user) {
        if (user.getCreatedDate() == null) {
            user.setCreatedDate(new Date(System.currentTimeMillis()));
        }
        userRepository.save(user);
    }

    @Override
    public void adminUpdate(User user) {
        Optional<User> opt = userRepository.findById(user.getId());
        if (opt.isPresent()) {
            User existingUser = opt.get();
            existingUser.setEmail(user.getEmail());
            existingUser.setFullName(user.getFullName());
            existingUser.setPhone(user.getPhone());
            existingUser.setRoleid(user.getRoleid());
            existingUser.setIsActive(user.getIsActive());

            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }

            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                if (existingUser.getAvatar() != null && !existingUser.getAvatar().isEmpty()) {
                    File oldAvatar = new File(Constant.DIR + "/" + existingUser.getAvatar());
                    if (oldAvatar.exists()) {
                        oldAvatar.delete();
                    }
                }
                existingUser.setAvatar(user.getAvatar());
            }
            userRepository.save(existingUser);
        }
    }
}
