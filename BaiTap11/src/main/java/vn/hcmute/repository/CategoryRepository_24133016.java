package vn.hcmute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hcmute.entity.Category_24133016;

@Repository
public interface CategoryRepository_24133016 extends JpaRepository<Category_24133016, Integer> {
    List<Category_24133016> findByStatus(Boolean status);
}
