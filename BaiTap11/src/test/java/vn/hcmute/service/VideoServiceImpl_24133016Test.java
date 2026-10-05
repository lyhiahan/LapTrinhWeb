package vn.hcmute.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.repository.FavoriteRepository_24133016;
import vn.hcmute.repository.ShareRepository_24133016;
import vn.hcmute.repository.VideoRepository_24133016;
import vn.hcmute.service.impl.VideoServiceImpl_24133016;

@ExtendWith(MockitoExtension.class)
class VideoServiceImpl_24133016Test {

    @Mock
    private VideoRepository_24133016 videoRepository;

    @Mock
    private FavoriteRepository_24133016 favoriteRepository;

    @Mock
    private ShareRepository_24133016 shareRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private VideoServiceImpl_24133016 videoService;

    @Test
    void insertWithoutIdGeneratesNextIdUsingVdThreeDigitFormat() {
        Video_24133016 video = new Video_24133016();
        when(videoRepository.findAllVideoIds()).thenReturn(List.of("VD001", "V07", "VD009", "LEGACY"));
        when(videoRepository.existsById("VD010")).thenReturn(false);

        Video_24133016 result = videoService.insert(video);

        assertSame(video, result);
        assertEquals("VD010", video.getVideoId());
        verify(entityManager).persist(video);
        verify(entityManager).flush();
    }

    @Test
    void insertWithExistingIdIsRejectedAndNeverOverwritesExistingVideo() {
        Video_24133016 video = new Video_24133016();
        video.setVideoId(" VD001 ");
        when(videoRepository.existsById("VD001")).thenReturn(true);

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> videoService.insert(video));

        assertEquals("Mã video VD001 đã tồn tại.", error.getMessage());
        verify(entityManager, never()).persist(video);
        verify(entityManager, never()).flush();
        verify(videoRepository, never()).save(video);
        verify(videoRepository, never()).saveAndFlush(video);
    }

    @Test
    void deleteRemovesFavoritesAndSharesBeforeVideo() throws Exception {
        String videoId = "VD010";
        when(videoRepository.existsById(videoId)).thenReturn(true);

        videoService.delete(videoId);

        InOrder order = inOrder(videoRepository, favoriteRepository, shareRepository);
        order.verify(videoRepository).existsById(videoId);
        order.verify(favoriteRepository).deleteByVideoId(videoId);
        order.verify(shareRepository).deleteByVideoId(videoId);
        order.verify(videoRepository).deleteById(videoId);
        order.verify(videoRepository).flush();
    }
}
