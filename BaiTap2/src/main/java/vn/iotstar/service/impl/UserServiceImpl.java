package vn.iotstar.service.impl;

import java.sql.Date;

import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;

public class UserServiceImpl implements IUserService {

    private IUserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = this.findByUsername(username);
        if (user != null && password.equals(user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        userDao.insert(new User(email, username, fullname, password, null, 5, phone, date));
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
}
