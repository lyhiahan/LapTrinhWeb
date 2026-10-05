package vn.hcmute.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hcmute.entity.OrderStatus_24133016;
import vn.hcmute.entity.Order_24133016;

@Repository
public interface OrderRepository_24133016 extends JpaRepository<Order_24133016, Long> {
    @Query("SELECT DISTINCT o FROM Order_24133016 o LEFT JOIN FETCH o.items " +
           "WHERE o.user.username = :username ORDER BY o.createdAt DESC")
    List<Order_24133016> findHistory(@Param("username") String username);

    @Query("SELECT DISTINCT o FROM Order_24133016 o LEFT JOIN FETCH o.items " +
           "WHERE o.user.username = :username AND o.status = :status ORDER BY o.createdAt DESC")
    List<Order_24133016> findHistoryByStatus(@Param("username") String username,
                                             @Param("status") OrderStatus_24133016 status);
}
