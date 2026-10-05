package vn.hcmute.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import vn.hcmute.entity.Video_24133016;

@Repository
public interface VideoRepository_24133016 extends JpaRepository<Video_24133016, String> {
    List<Video_24133016> findByCategory_CategoryId(int categoryId);
    Page<Video_24133016> findByCategory_CategoryId(int categoryId, Pageable pageable);
    Page<Video_24133016> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT COUNT(v) FROM Video_24133016 v WHERE v.category.categoryId = :categoryId")
    long countByCategoryId(@Param("categoryId") int categoryId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Video_24133016 v WHERE v.videoId = :videoId")
    Video_24133016 findByIdForUpdate(@Param("videoId") String videoId);
}
