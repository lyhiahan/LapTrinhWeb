package vn.hcmute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.hcmute.entity.Share_24133016;

@Repository
public interface ShareRepository_24133016 extends JpaRepository<Share_24133016, Integer> {

    @Query("SELECT COUNT(s) FROM Share_24133016 s WHERE s.video.videoId = :videoId")
    long countByVideoId(@Param("videoId") String videoId);
}
