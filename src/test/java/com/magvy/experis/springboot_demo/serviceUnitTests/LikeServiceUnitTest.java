package com.magvy.experis.springboot_demo.serviceUnitTests;

import com.magvy.experis.javalava_backend.domain.entitites.Like;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.entitites.composite.LikeId;
import com.magvy.experis.javalava_backend.domain.exceptions.LikeException;
import com.magvy.experis.javalava_backend.domain.services.LikeService;
import com.magvy.experis.javalava_backend.domain.services.WebSocketService;
import com.magvy.experis.javalava_backend.domain.util.LikeUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class LikeServiceUnitTest {

    @Mock
    LikeRepository likeRepository;

    @Mock
    LikeUtil likeUtil;

    @Mock
    WebSocketService webSocketService;

    LikeService likeService;

    AutoCloseable mocks;

    Long userId = 1L;
    Long postId = 2L;

    // Initialize the user and post with some arbitrary content (not important)
    User user = new User(userId, "Tom Bombadill", "MyPassword");
    Post post = new Post(postId, "Some content", LocalDateTime.now(), true, user);
    LikeId id = new LikeId(user, post);
    Like like = new Like(user, post);


    @BeforeEach
    void setUp(){
        mocks = MockitoAnnotations.openMocks(this);
        likeService = new LikeService(likeRepository, likeUtil, webSocketService);
        when(likeUtil.createLikeId(anyLong())).thenReturn(id);
        when(likeUtil.convertToEntity(anyLong())).thenReturn(like);
        when(likeRepository.save(any(Like.class))).thenReturn(like);
    }

    @AfterEach
    void breakDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    void likePostSavesLike_WhenLikeDoesntExist() {
        when(likeRepository.existsById(id)).thenReturn(false);

        // Method so we can see the like-object passed to the save-parameter
        when(likeRepository.save(any(Like.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Should not throw if user can like post
        assertDoesNotThrow(() -> {
            likeService.likePost(postId);
        });

        ArgumentCaptor<Like> captor = ArgumentCaptor.forClass(Like.class);
        // Save invocation to captor
        verify(likeRepository, times(1)).save(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(captor.getValue().getUser(), user);
        assertEquals(captor.getValue().getPost(), post);
    }

    @Test
    void likePostReturnConflict_WhenLikeAlreadyExist() {
        // Like already exists
        when(likeRepository.existsById(id)).thenReturn(true);

        assertThrows(LikeException.class, () -> likeService.likePost(postId));
        verify(likeRepository, never()).save(any(Like.class));
    }


    @Test
    void unLikePostReturnOkOrNoContent_WhenLikeExists() {
        when(likeRepository.existsById(id)).thenReturn(true);

        doAnswer(invocation -> invocation.getArgument(0)).when(likeRepository).delete(any(Like.class));

        assertDoesNotThrow(() -> {
            likeService.unlikePost(postId);
        });

        ArgumentCaptor<Like> captor = ArgumentCaptor.forClass(Like.class);
        verify(likeRepository, times(1)).delete(captor.capture());
        Like deletedLike = captor.getValue();
        assertNotNull(deletedLike);
        assertEquals(deletedLike.getPost(), post);
        assertEquals(deletedLike.getUser(), user);
    }

    @Test
    void unLikePostReturnNotFound_WhenLikeDoesntExist() {
        when(likeRepository.existsById(id)).thenReturn(false);
        assertThrows(LikeException.class, () -> likeService.unlikePost(postId));
        verify(likeRepository, never()).delete(any(Like.class));
    }
}
