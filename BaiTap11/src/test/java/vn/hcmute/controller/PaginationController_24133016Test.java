package vn.hcmute.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import vn.hcmute.entity.Category_24133016;
import vn.hcmute.entity.User_24133016;
import vn.hcmute.entity.Video_24133016;
import vn.hcmute.service.ICategoryService_24133016;
import vn.hcmute.service.IFavoriteService_24133016;
import vn.hcmute.service.IShareService_24133016;
import vn.hcmute.service.IVideoService_24133016;

class PaginationController_24133016Test {

    @Test
    @SuppressWarnings("unchecked")
    void homeClampsOversizedCategoryPageAndSortsByVideoId() {
        ICategoryService_24133016 categoryService = mock(ICategoryService_24133016.class);
        IVideoService_24133016 videoService = mock(IVideoService_24133016.class);
        IShareService_24133016 shareService = mock(IShareService_24133016.class);
        IFavoriteService_24133016 favoriteService = mock(IFavoriteService_24133016.class);

        HomeController_24133016 controller = new HomeController_24133016();
        ReflectionTestUtils.setField(controller, "categoryService", categoryService);
        ReflectionTestUtils.setField(controller, "videoService", videoService);
        ReflectionTestUtils.setField(controller, "shareService", shareService);
        ReflectionTestUtils.setField(controller, "favoriteService", favoriteService);

        Category_24133016 category = new Category_24133016();
        category.setCategoryId(7);
        Video_24133016 lastVideo = new Video_24133016();
        lastVideo.setVideoId("VID008");
        when(categoryService.findAll()).thenReturn(List.of(category));
        when(videoService.countByCategoryId(7)).thenReturn(8L);
        when(videoService.findByCategoryId(eq(7), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(lastVideo), PageRequest.of(2, 3), 8));

        ExtendedModelMap model = new ExtendedModelMap();
        assertEquals("home", controller.home(model, Integer.MAX_VALUE, 7));

        List<Map<String, Object>> categoryData =
                (List<Map<String, Object>>) model.get("categoryDataList");
        assertNotNull(categoryData);
        assertEquals(3, categoryData.get(0).get("currentPage"));

        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        org.mockito.Mockito.verify(videoService).findByCategoryId(eq(7), pageable.capture());
        assertEquals(2, pageable.getValue().getPageNumber());
        assertEquals(3, pageable.getValue().getPageSize());
        assertEquals(Sort.Direction.ASC,
                pageable.getValue().getSort().getOrderFor("videoId").getDirection());
    }

    @Test
    void adminClampsOversizedPageWithoutBuildingAnUnsafeOffset() {
        IVideoService_24133016 videoService = mock(IVideoService_24133016.class);
        ICategoryService_24133016 categoryService = mock(ICategoryService_24133016.class);
        AdminVideoController_24133016 controller = new AdminVideoController_24133016();
        ReflectionTestUtils.setField(controller, "videoService", videoService);
        ReflectionTestUtils.setField(controller, "categoryService", categoryService);

        Video_24133016 lastVideo = new Video_24133016();
        lastVideo.setVideoId("VID013");
        when(videoService.findAll(any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            List<Video_24133016> content = pageable.getPageNumber() == 2
                    ? List.of(lastVideo) : List.of();
            return new PageImpl<>(content, pageable, 13);
        });

        User_24133016 admin = new User_24133016();
        admin.setAdmin(true);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", admin);
        ExtendedModelMap model = new ExtendedModelMap();

        assertEquals("admin/video-list",
                controller.listVideos(Integer.MAX_VALUE, null, session, model));
        assertEquals(3, model.get("currentPage"));

        ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
        org.mockito.Mockito.verify(videoService, org.mockito.Mockito.times(2))
                .findAll(pageable.capture());
        assertEquals(List.of(0, 2), pageable.getAllValues().stream()
                .map(Pageable::getPageNumber).toList());
        for (Pageable request : pageable.getAllValues()) {
            assertEquals(Sort.Direction.ASC,
                    request.getSort().getOrderFor("videoId").getDirection());
        }
    }

    @Test
    void nonPositivePagesAreClampedToFirstPage() {
        IVideoService_24133016 videoService = mock(IVideoService_24133016.class);
        AdminVideoController_24133016 controller = new AdminVideoController_24133016();
        ReflectionTestUtils.setField(controller, "videoService", videoService);
        ReflectionTestUtils.setField(controller, "categoryService",
                mock(ICategoryService_24133016.class));
        when(videoService.findAll(any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            return new PageImpl<>(List.of(), pageable, 1);
        });

        User_24133016 admin = new User_24133016();
        admin.setAdmin(true);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("account", admin);
        ExtendedModelMap model = new ExtendedModelMap();

        assertEquals("admin/video-list", controller.listVideos(0, null, session, model));
        assertEquals(1, model.get("currentPage"));
    }
}
