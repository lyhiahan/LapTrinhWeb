package vn.hcmute.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hcmute.repository.ShareRepository_24133016;
import vn.hcmute.service.IShareService_24133016;

@Service
@Transactional
public class ShareServiceImpl_24133016 implements IShareService_24133016 {

    @Autowired
    private ShareRepository_24133016 shareRepository;

    @Override
    public long countByVideoId(String videoId) {
        return shareRepository.countByVideoId(videoId);
    }
}
