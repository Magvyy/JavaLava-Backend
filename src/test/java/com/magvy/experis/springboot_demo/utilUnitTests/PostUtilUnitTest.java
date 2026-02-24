package com.magvy.experis.springboot_demo.utilUnitTests;

import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.util.FriendUtil;
import com.magvy.experis.javalava_backend.domain.util.PostUtil;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PostUtilUnitTest {
    @Mock
    PostRepository postRepository;

    @Mock
    LikeRepository likeRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    SecurityUtil securityUtil;

    @Mock
    FriendUtil friendUtil;

    AutoCloseable mocks;

    PostUtil postUtil;

    @BeforeEach
    void setup(){
        mocks = MockitoAnnotations.openMocks(this);
        postUtil = new PostUtil(postRepository, likeRepository, commentRepository, securityUtil, friendUtil);
        user = new User(1L, "", "");
        postOwner = new User(2L, "", "");
        post = new Post(1L, "", LocalDateTime.now(), true, postOwner);
    }

    @AfterEach
    void breakDown() throws Exception {
        if (mocks != null) mocks.close();
    }

    User user;
    User postOwner;
    Post post;

    @Test
    void isPostVisibleToUser_WhenPostNotVisible_ReturnsFalse(){
        post.setVisible(false);
        when(friendUtil.isFriends(anyLong())).thenReturn(false);

        when(securityUtil.getAuthenticatedUser()).thenReturn(null);
        assertFalse(postUtil.isPostVisibleToAuthenticatedUser(post));

        when(securityUtil.getAuthenticatedUser()).thenReturn(user);
        assertFalse(postUtil.isPostVisibleToAuthenticatedUser(post));
    }

    @Test
    void isPostVisibleToUser_WhenPostVisible_ReturnsTrue(){
        when(securityUtil.getAuthenticatedUser()).thenReturn(null);
        assertFalse(postUtil.isPostVisibleToAuthenticatedUser(post));

        when(securityUtil.getAuthenticatedUser()).thenReturn(user);
        assertFalse(postUtil.isPostVisibleToAuthenticatedUser(post));

        verify(friendUtil, never()).isFriends(anyLong());
    }

    @Test
    void isPostVisibleToUser_WhenUserAndCreatorFriendsAndPostNotVisible_ReturnsTrue(){
        post.setVisible(false);
        when(securityUtil.getAuthenticatedUser()).thenReturn(user);
        when(friendUtil.isFriends(anyLong())).thenAnswer(invocation -> {
            Long userId = invocation.getArgument(0);
            return userId.equals(postOwner.getId());
        });
        assertTrue(postUtil.isPostVisibleToAuthenticatedUser(post));
        verify(friendUtil, times(1)).isFriends(anyLong());
    }

    @Test
    void isPostVisibleToUser_WhenUserIsCreatorAndPostNotVisible_ReturnsTrue(){
        post.setVisible(false);
        when(securityUtil.getAuthenticatedUser()).thenReturn(postOwner);
        assertTrue(postUtil.isPostVisibleToAuthenticatedUser(post));
        verify(friendUtil, never()).isFriends(anyLong());
    }
}
