package vn.iotstar.service.impl;

import java.sql.Date;

import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.util.EmailUtil;

public class UserServiceImpl implements IUserService {

    private IUserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = this.findByUsername(username);
        if (user != null && password.equals(user.getPassWord())) {
            if (!user.getIsActive()) {
                return null;
            }
            return user;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        User user = new User(email, username, fullname, password, null, 5, phone, date);
        user.setIsActive(false);
        userDao.insert(user);

        sendOtp(email);
        return true;
    }

    @Override
    public boolean sendOtp(String email) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return false;
        }

        String otp = EmailUtil.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(new java.util.Date(System.currentTimeMillis() + 120 * 1000));
        userDao.update(user);

        return EmailUtil.sendOtpEmail(email, otp, "Mã xác thực OTP - ShoppingAdmin");
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userDao.findByEmail(email);
        if (user == null || user.getOtp() == null) {
            return false;
        }

        if (otp.equals(user.getOtp()) && user.getOtpExpiry() != null
                && user.getOtpExpiry().after(new java.util.Date())) {
            user.setIsActive(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            userDao.update(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            return false;
        }
        user.setPassWord(newPassword);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userDao.update(user);
        return true;
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public void updateProfile(User user) {
        User existingUser = userDao.findByUsername(user.getUserName());
        if (existingUser != null) {
            existingUser.setFullName(user.getFullName());
            existingUser.setPhone(user.getPhone());
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                existingUser.setAvatar(user.getAvatar());
            }
            userDao.update(existingUser);
        }
    }
}
