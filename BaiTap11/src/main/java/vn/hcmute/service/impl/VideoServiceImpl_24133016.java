package vn.hcmute.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hcmute.entity.Video_24133016;
import vn.hcmute.repository.VideoRepository_24133016;
import vn.hcmute.service.IVideoService_24133016;

@Service
@Transactional
public class VideoServiceImpl_24133016 implements IVideoService_24133016 {

    @Autowired
    private VideoRepository_24133016 videoRepository;

    @Override
    public List<Video_24133016> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public Video_24133016 findById(String videoId) {
        return videoRepository.findById(videoId).orElse(null);
    }

    @Override
    public void insert(Video_24133016 video) {
        videoRepository.save(video);
    }

    @Override
    public void update(Video_24133016 video) {
        videoRepository.save(video);
    }

    @Override
    public void delete(String videoId) throws Exception {
        if (videoRepository.existsById(videoId)) {
            videoRepository.deleteById(videoId);
        } else {
            throw new Exception("Không tìm thấy Video với ID: " + videoId);
        }
    }

    @Override
    public List<Video_24133016> findByCategoryId(int categoryId) {
        return videoRepository.findByCategory_CategoryId(categoryId);
    }

    @Override
    public Page<Video_24133016> findByCategoryId(int categoryId, Pageable pageable) {
        return videoRepository.findByCategory_CategoryId(categoryId, pageable);
    }

    @Override
    public Page<Video_24133016> findAll(Pageable pageable) {
        return videoRepository.findAll(pageable);
    }

    @Override
    public Page<Video_24133016> search(String keyword, Pageable pageable) {
        return videoRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public long countByCategoryId(int categoryId) {
        return videoRepository.countByCategoryId(categoryId);
    }

    @Override
    public long count() {
        return videoRepository.count();
    }
}
