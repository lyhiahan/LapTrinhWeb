package vn.hcmute.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hcmute.repository.FavoriteRepository_24133016;
import vn.hcmute.service.IFavoriteService_24133016;

@Service
@Transactional
public class FavoriteServiceImpl_24133016 implements IFavoriteService_24133016 {

    @Autowired
    private FavoriteRepository_24133016 favoriteRepository;

    @Override
    public long countByVideoId(String videoId) {
        return favoriteRepository.countByVideoId(videoId);
    }
}
