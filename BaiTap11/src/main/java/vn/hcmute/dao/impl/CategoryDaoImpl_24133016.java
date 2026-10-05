package vn.hcmute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import vn.hcmute.config.JPAConfig_24133016;
import vn.hcmute.dao.ICategoryDao_24133016;
import vn.hcmute.entity.Category_24133016;

public class CategoryDaoImpl_24133016 implements ICategoryDao_24133016 {

    @Override
    public void insert(Category_24133016 category) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(category);
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
    public void update(Category_24133016 category) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(category);
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
    public void delete(int id) throws Exception {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Category_24133016 category = enma.find(Category_24133016.class, id);
            if (category != null) {
                enma.remove(category);
            } else {
                throw new Exception("Không tìm thấy Category với ID: " + id);
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
    public Category_24133016 findById(int id) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            return enma.find(Category_24133016.class, id);
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Category_24133016> findAll() {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            TypedQuery<Category_24133016> query = enma.createQuery("SELECT c FROM Category_24133016 c", Category_24133016.class);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public int count() {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            return ((Long) enma.createQuery("SELECT COUNT(c) FROM Category_24133016 c").getSingleResult()).intValue();
        } finally {
            enma.close();
        }
    }
}
