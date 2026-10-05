package vn.hcmute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.hcmute.entity.Favorite_24133016;

@Repository
public interface FavoriteRepository_24133016 extends JpaRepository<Favorite_24133016, Integer> {

    @Query("SELECT COUNT(f) FROM Favorite_24133016 f WHERE f.video.videoId = :videoId")
    long countByVideoId(@Param("videoId") String videoId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Favorite_24133016 f WHERE f.video.videoId = :videoId")
    int deleteByVideoId(@Param("videoId") String videoId);
}
