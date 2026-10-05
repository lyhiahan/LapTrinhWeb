package vn.hcmute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import vn.hcmute.config.JPAConfig_24133016;
import vn.hcmute.dao.IVideoDao_24133016;
import vn.hcmute.entity.Video_24133016;

public class VideoDaoImpl_24133016 implements IVideoDao_24133016 {

    @Override
    public void insert(Video_24133016 video) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(video);
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
    public void update(Video_24133016 video) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(video);
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
    public void delete(String videoId) throws Exception {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Video_24133016 video = enma.find(Video_24133016.class, videoId);
            if (video != null) {
                enma.remove(video);
            } else {
                throw new Exception("Không tìm thấy Video với ID: " + videoId);
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
    public Video_24133016 findById(String videoId) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            return enma.find(Video_24133016.class, videoId);
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video_24133016> findAll() {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            TypedQuery<Video_24133016> query = enma.createQuery("SELECT v FROM Video_24133016 v", Video_24133016.class);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video_24133016> findByCategoryId(int categoryId) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            TypedQuery<Video_24133016> query = enma.createQuery(
                "SELECT v FROM Video_24133016 v WHERE v.category.categoryId = :categoryId", Video_24133016.class);
            query.setParameter("categoryId", categoryId);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public int count() {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            return ((Long) enma.createQuery("SELECT COUNT(v) FROM Video_24133016 v").getSingleResult()).intValue();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video_24133016> findPaginated(int page, int pageSize) {
        EntityManager enma = JPAConfig_24133016.getEntityManager();
        try {
            TypedQuery<Video_24133016> query = enma.createQuery(
                "SELECT v FROM Video_24133016 v ORDER BY v.videoId", Video_24133016.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }
}
