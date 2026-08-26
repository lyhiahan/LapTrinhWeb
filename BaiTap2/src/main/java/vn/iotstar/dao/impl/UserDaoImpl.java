package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import vn.iotstar.config.JPAConfig;
import vn.iotstar.dao.IUserDao;
import vn.iotstar.entity.User;

public class UserDaoImpl implements IUserDao {

    @Override
    public User findByUsername(String username) {
        EntityManager enma = JPAConfig.getEntityManager();
        String jpql = "SELECT u FROM User u WHERE u.userName = :username";
        try {
            TypedQuery<User> query = enma.createQuery(jpql, User.class);
            query.setParameter("username", username);
            List<User> list = query.getResultList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        } finally {
            enma.close();
        }
    }

    @Override
    public void insert(User user) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(user);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistEmail(String email) {
        EntityManager enma = JPAConfig.getEntityManager();
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        try {
            TypedQuery<Long> query = enma.createQuery(jpql, Long.class);
            query.setParameter("email", email);
            Long count = query.getSingleResult();
            return count > 0;
        } finally {
            enma.close();
        }
    }

    @Override
    public boolean checkExistUsername(String username) {
        EntityManager enma = JPAConfig.getEntityManager();
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.userName = :username";
        try {
            TypedQuery<Long> query = enma.createQuery(jpql, Long.class);
            query.setParameter("username", username);
            Long count = query.getSingleResult();
            return count > 0;
        } finally {
            enma.close();
        }
    }
}
