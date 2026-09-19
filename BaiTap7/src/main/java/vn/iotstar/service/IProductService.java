package vn.iotstar.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.iotstar.entity.Product;

public interface IProductService {
    void delete(Product entity);
    void deleteById(Long id);
    long count();
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findAll(Sort sort);
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findByProductName(String name);
    Optional<Product> findByCreateDate(Date createAt);
    <S extends Product> S save(S entity);
    List<Product> findByProductNameContaining(String name);
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
}
