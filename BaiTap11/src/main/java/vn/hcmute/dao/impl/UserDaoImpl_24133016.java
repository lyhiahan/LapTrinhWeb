package vn.hcmute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import vn.hcmute.config.JPAConfig_24133016;
import vn.hcmute.dao.IUserDao_24133016;
import vn.hcmute.entity.User_24133016;

public class UserDaoImpl_24133016 implements IUserDao_24133016 {

    @Override
    public void insert(User_24133016 user) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(user);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void update(User_24133016 user) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(user);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public void delete(String username) throws Exception {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            User_24133016 user = enma.find(User_24133016.class, username);
            if (user != null) {
                enma.remove(user);
            } else {
                throw new Exception("Không tìm thấy User: " + username);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public User_24133016 findById(String username) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            return enma.find(User_24133016.class, username);
        } finally {
            enma.close();
        }
    }

    @Override
    public User_24133016 findByEmail(String email) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            TypedQuery<User_24133016> query = enma.createQuery(
                "SELECT u FROM User_24133016 u WHERE u.email = :email", User_24133016.class);
            query.setParameter("email", email);
            List<User_24133016> list = query.getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            enma.close();
        }
    }

    @Override
    public List<User_24133016> findAll() {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            TypedQuery<User_24133016> query = enma.createQuery("SELECT u FROM User_24133016 u", User_24133016.class);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public User_24133016 login(String username, String password) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            TypedQuery<User_24133016> query = enma.createQuery(
                "SELECT u FROM User_24133016 u WHERE u.username = :username AND u.password = :password AND u.active = true",
                User_24133016.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            List<User_24133016> list = query.getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            enma.close();
        }
    }
}
