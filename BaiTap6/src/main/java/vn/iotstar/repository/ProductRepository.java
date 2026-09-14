package vn.iotstar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategory_Id(int categoryId);
}
