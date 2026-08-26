package vn.iotstar.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import vn.iotstar.config.JPAConfig;
import vn.iotstar.entity.Category;

public class TestJpa {
    public static void main(String[] args) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();

        Category cate = new Category();
        cate.setName("Điện thoại");
        cate.setIcon("https://i.imgur.com/vHqJ69m.png");
        cate.setPrice(15000000);

        try {
            trans.begin();
            enma.persist(cate);
            trans.commit();
            System.out.println("✅ THÊM THÀNH CÔNG CATEGORY BẰNG JPA!");
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) {
                trans.rollback();
            }
        } finally {
            enma.close();
        }
    }
}
