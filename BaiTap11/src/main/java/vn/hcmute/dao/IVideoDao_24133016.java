package vn.hcmute.dao;

import java.util.List;
import vn.hcmute.entity.Video_24133016;

public interface IVideoDao_24133016 {
    void insert(Video_24133016 video);
    void update(Video_24133016 video);
    void delete(String videoId) throws Exception;
    Video_24133016 findById(String videoId);
    List<Video_24133016> findAll();
    List<Video_24133016> findByCategoryId(int categoryId);
    int count();
    List<Video_24133016> findPaginated(int page, int pageSize);
}
