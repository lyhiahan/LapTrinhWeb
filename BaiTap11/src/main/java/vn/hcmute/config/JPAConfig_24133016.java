package vn.hcmute.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAConfig_24133016 {

    private static final EntityManagerFactory factory = createFactory();

    private static EntityManagerFactory createFactory() {
        Map<String, Object> properties = new HashMap<>();
        String dbUrl = System.getenv("DB_URL");
        String dbUsername = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbUrl != null && !dbUrl.isBlank()) {
            properties.put("jakarta.persistence.jdbc.url", dbUrl);
        }
        if (dbUsername != null && !dbUsername.isBlank()) {
            properties.put("jakarta.persistence.jdbc.user", dbUsername);
        }
        if (dbPassword != null) {
            properties.put("jakarta.persistence.jdbc.password", dbPassword);
        }
        return Persistence.createEntityManagerFactory("jpa-hibernate-sqlserver", properties);
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
