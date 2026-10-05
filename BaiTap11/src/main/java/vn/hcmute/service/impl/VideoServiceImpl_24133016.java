package vn.hcmute.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.repository.FavoriteRepository_24133016;
import vn.hcmute.repository.ShareRepository_24133016;
import vn.hcmute.repository.VideoRepository_24133016;
import vn.hcmute.service.IVideoService_24133016;

@Service
@Transactional
public class VideoServiceImpl_24133016 implements IVideoService_24133016 {

    private static final Pattern TRAILING_NUMBER = Pattern.compile("(\\d+)$");

    @Autowired
    private VideoRepository_24133016 videoRepository;

    @Autowired
    private FavoriteRepository_24133016 favoriteRepository;

    @Autowired
    private ShareRepository_24133016 shareRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Video_24133016> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public Video_24133016 findById(String videoId) {
        return videoRepository.findById(videoId).orElse(null);
    }

    @Override
    public synchronized Video_24133016 insert(Video_24133016 video) {
        if (video == null) {
            throw new IllegalArgumentException("Dữ liệu video không hợp lệ.");
        }

        String requestedId = video.getVideoId();
        if (requestedId == null || requestedId.isBlank()) {
            video.setVideoId(generateNextVideoId());
        } else {
            requestedId = requestedId.trim();
            if (videoRepository.existsById(requestedId)) {
                throw new IllegalArgumentException("Mã video " + requestedId + " đã tồn tại.");
            }
            video.setVideoId(requestedId);
        }

        // persist luôn tạo bản ghi mới; khác với save/merge, nó không thể âm thầm
        // ghi đè một video đã tồn tại nếu xảy ra trùng khóa chính.
        entityManager.persist(video);
        entityManager.flush();
        return video;
    }

    @Override
    public void update(Video_24133016 video) {
        if (video == null || video.getVideoId() == null
                || !videoRepository.existsById(video.getVideoId())) {
            throw new IllegalArgumentException("Không tìm thấy video cần cập nhật.");
        }
        videoRepository.saveAndFlush(video);
    }

    @Override
    public void delete(String videoId) throws Exception {
        if (videoId == null || videoId.isBlank() || !videoRepository.existsById(videoId)) {
            throw new IllegalArgumentException("Không tìm thấy Video với ID: " + videoId);
        }

        // Xóa các bản ghi phụ thuộc trong cùng một transaction trước khi xóa video.
        // Nếu bất kỳ thao tác nào thất bại, toàn bộ thay đổi trong database được rollback.
        favoriteRepository.deleteByVideoId(videoId);
        shareRepository.deleteByVideoId(videoId);
        videoRepository.deleteById(videoId);
        videoRepository.flush();
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

    private String generateNextVideoId() {
        long maxNumber = 0;
        for (String id : videoRepository.findAllVideoIds()) {
            if (id == null) continue;
            Matcher matcher = TRAILING_NUMBER.matcher(id.trim());
            if (!matcher.find()) continue;
            try {
                maxNumber = Math.max(maxNumber, Long.parseLong(matcher.group(1)));
            } catch (NumberFormatException ignored) {
                // Bỏ qua mã cũ có phần số vượt giới hạn long.
            }
        }

        long nextNumber = maxNumber + 1;
        if (nextNumber <= 0) {
            throw new IllegalStateException("Không thể sinh mã video tiếp theo.");
        }

        String candidate;
        do {
            candidate = String.format("VD%03d", nextNumber++);
        } while (videoRepository.existsById(candidate));
        return candidate;
    }
}
