package vn.hcmute.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.hcmute.entity.Video_24133016;

public interface IVideoService_24133016 {
    List<Video_24133016> findAll();
    Video_24133016 findById(String videoId);
    Video_24133016 insert(Video_24133016 video);
    void update(Video_24133016 video);
    void delete(String videoId) throws Exception;
    List<Video_24133016> findByCategoryId(int categoryId);
    Page<Video_24133016> findByCategoryId(int categoryId, Pageable pageable);
    Page<Video_24133016> findAll(Pageable pageable);
    Page<Video_24133016> search(String keyword, Pageable pageable);
    long countByCategoryId(int categoryId);
    long count();
}
