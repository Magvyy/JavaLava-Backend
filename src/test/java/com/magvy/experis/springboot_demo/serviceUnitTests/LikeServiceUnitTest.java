package com.magvy.experis.springboot_demo.serviceUnitTests;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.LikeDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Like;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.LikeService;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LikeServiceUnitTest {

    @Mock
    LikeRepository likeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PostRepository postRepository;

    LikeService likeService;

    AutoCloseable mocks;

    Long user_id = 1L;
    Long post_id = 2L;
    LikeDTORequest likeDtoRequest = new LikeDTORequest(user_id, post_id);

    // Initialize the user and post with some arbitrary content (not important)
    User user = new User(user_id, "Tom Bombadill", "MyPassword");
    Post post = new Post(post_id, "Some content", LocalDateTime.now(), true, user);


    @BeforeEach
    void setUp(){
        mocks = MockitoAnnotations.openMocks(this);
        likeService = new LikeService(likeRepository, userRepository, postRepository);
    }

    @AfterEach
    void breakDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    @Test
    void likePostSavesLike_WhenLikeDoesntExist() {
        when(likeRepository.existsByPost_idAndUser_Id(post_id, user_id)).thenReturn(false);
        when(userRepository.getReferenceById(user_id)).thenReturn(user);
        when(postRepository.getReferenceById(post_id)).thenReturn(post);
        // Method so we can see the like-object passed to the save-parameter
        when(likeRepository.save(any(Like.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<String> response = likeService.likePost(likeDtoRequest);

        // Should return either 204 no content, or 200 ok
        assertTrue(HttpStatus.NO_CONTENT.equals(response.getStatusCode()) || HttpStatus.OK.equals(response.getStatusCode()));

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
        when(likeRepository.existsByPost_idAndUser_Id(post_id, user_id)).thenReturn(true);

        ResponseEntity<String> response = likeService.likePost(likeDtoRequest);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(likeRepository, never()).save(any(Like.class));
    }


    @Test
    void unLikePostReturnOkOrNoContent_WhenLikeExists() {
        when(likeRepository.existsByPost_idAndUser_Id(post_id, user_id)).thenReturn(true);
        when(userRepository.getReferenceById(user_id)).thenReturn(user);
        when(postRepository.getReferenceById(post_id)).thenReturn(post);
        doAnswer(invocation -> invocation.getArgument(0)).when(likeRepository).delete(any(Like.class));

        LikeDTORequest dto = new LikeDTORequest(user_id, post_id);
        ResponseEntity<String> response = likeService.unlikePost(dto);
        assertTrue(HttpStatus.OK.equals(response.getStatusCode()) || HttpStatus.NO_CONTENT.equals(response.getStatusCode()));

        ArgumentCaptor<Like> captor = ArgumentCaptor.forClass(Like.class);
        verify(likeRepository, times(1)).delete(captor.capture());
        Like deletedLike = captor.getValue();
        assertNotNull(deletedLike);
        assertEquals(deletedLike.getPost(), post);
        assertEquals(deletedLike.getUser(), user);
    }

}
