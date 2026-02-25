package com.magvy.experis.springboot_demo.serviceUnitTests;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PostDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.services.PostService;
import com.magvy.experis.javalava_backend.domain.util.AttachmentUtil;
import com.magvy.experis.javalava_backend.domain.util.PostUtil;
import com.magvy.experis.javalava_backend.domain.util.SecurityUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceUnitTest {
    @Mock
    PostRepository postRepository;

    @Mock
    SecurityUtil securityUtil;

    @Mock
    PostUtil postUtil;

    @Mock
    AttachmentUtil attachmentUtil;

    AutoCloseable mocks;

    PostService postService;

    @BeforeEach
    void setup(){
        mocks = MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository, securityUtil, postUtil, attachmentUtil);
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
    void loadPostByUserWhenUserIsEmpty_ReturnsAllFoundPostsByTargetOrdered() {
        Optional<User> user = Optional.empty();
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        Post post1 = new Post(1L, "post1 Content", LocalDateTime.of(2024, Month.JANUARY, 10, 1, 1), true, mockUser);
        Post post2 = new Post(2L, "post2 Content", LocalDateTime.of(2024, Month.FEBRUARY, 10, 1, 2), true, mockUser);
        Post post3 = new Post(3L, "post3 Content", LocalDateTime.of(2023, Month.DECEMBER, 31, 23, 59), true, mockUser);
        Post post4 = new Post(4L, "post4 Content", LocalDateTime.of(2024, Month.FEBRUARY, 10, 1, 1), true, mockUser);


        Page<Post> mockPost = new PageImpl<>(List.of(post2, post4, post1, post3));
        when(postRepository.findByVisibleTrueAndUserId(eq(mockUser.getId()), any(Pageable.class))).thenReturn(mockPost);

        when(postUtil.pageToDTOList(any(Page.class), anyLong())).thenAnswer(invocation -> {
           Page<Post> posts = invocation.getArgument(0);
           return posts.stream()
                   .map(post -> new PostDTOResponse(
                           post,
                           false,
                           0,
                           0
                   ));
        });

        List<PostDTOResponse> result = postService.loadPostsByUser(0, mockUser.getId());
        assertEquals(4, result.size());
        assertEquals(post2.getId(), result.getFirst().getId());
        assertEquals(post4.getId(), result.get(1).getId());
        assertEquals(post1.getId(), result.get(2).getId());
        assertEquals(post3.getId(), result.get(3).getId());
        verify(postRepository, never()).findPostsFromUser(anyLong(), anyLong(), any(Pageable.class));
    }

    @Test
    void loadPostByUserWhenUserExist_ReturnsAllFoundPostsByTargetOrderedWithLikeAndCommentData(){
        User mockTargetUser = mock(User.class);
        when(mockTargetUser.getId()).thenReturn(1L);
        User mockSelfUser = mock(User.class);
        Optional<User> user = Optional.of(mockSelfUser);
        when(mockSelfUser.getId()).thenReturn(2L);
        Post post1 = new Post(1L, "post1 Content", LocalDateTime.of(2024, Month.JANUARY, 10, 1, 1), true, mockTargetUser);
        Post post2 = new Post(2L, "post2 Content", LocalDateTime.of(2024, Month.FEBRUARY, 10, 1, 2), true, mockTargetUser);
        Post post3 = new Post(3L, "post3 Content", LocalDateTime.of(2023, Month.DECEMBER, 31, 23, 59), true, mockTargetUser);
        Post post4 = new Post(4L, "post4 Content", LocalDateTime.of(2024, Month.FEBRUARY, 10, 1, 1), true, mockTargetUser);

        Page<Post> mockPost = new PageImpl<>(List.of(post2, post4, post1, post3));
        when(postRepository.findPostsFromUser(argThat(id -> id.equals(mockSelfUser.getId())), argThat(id -> id.equals(mockTargetUser.getId())), any(Pageable.class))).thenReturn(mockPost);


        when(postUtil.pageToDTOList(any(Page.class), anyLong())).thenAnswer(invocation -> {
            Page<Post> posts = invocation.getArgument(0);
            Long userId = invocation.getArgument(1);
            return posts.stream()
                    .map(post -> new PostDTOResponse(
                            post,
                            userId.equals(mockSelfUser.getId()) && List.of(2L, 3L).contains(post.getId()),
                            post1.getId().equals(1L) ? 4 : 0,
                            5
                    ));
        });

        List<PostDTOResponse> result = postService.loadPostsByUser(0, mockTargetUser.getId());
        assertEquals(4, result.size());

        assertEquals(post2.getId(), result.getFirst().getId());
        assertEquals(post4.getId(), result.get(1).getId());
        assertEquals(post1.getId(), result.get(2).getId());
        assertEquals(post3.getId(), result.get(3).getId());

        assertTrue(result.getFirst().isLiked() && result.get(3).isLiked());
        assertFalse(result.get(1).isLiked() || result.get(2).isLiked());

        assertTrue(result.stream().allMatch(res -> res.getCommentCount() == 5L));
        assertTrue(result.get(2).getLikeCount() == 4L && result.get(1).getLikeCount() == 0L);

        verify(postRepository, never()).findByVisibleTrueAndUserId(anyLong(), any(Pageable.class));
    }

//    @Test
//    void isPostVisibleToUser_WhenPostNotVisible_ReturnsFalse(){
//        post.setVisible(false);
//        when(friendService.isFriends(anyLong(), anyLong())).thenReturn(false);
//        assertFalse(postService.isPostVisibleToUser(post, null));
//        assertFalse(postService.isPostVisibleToUser(post, user));
//    }
//
//    @Test
//    void isPostVisibleToUser_WhenPostVisible_ReturnsTrue(){
//        assertTrue(postService.isPostVisibleToUser(post, null));
//        assertTrue(postService.isPostVisibleToUser(post, user));
//        verify(friendService, never()).isFriends(anyLong(), anyLong());
//    }
//
//    @Test
//    void isPostVisibleToUser_WhenUserAndCreatorFriendsAndPostNotVisible_ReturnsTrue(){
//        post.setVisible(false);
//        when(friendService.isFriends(anyLong(), anyLong())).thenAnswer(invocation -> {
//            Long userId1 = invocation.getArgument(0);
//            Long userId2 = invocation.getArgument(1);
//            if ((userId1 == 1L && userId2 == 2L) || (userId1 == 2L && userId2 == 1L)) return true;
//            else return false;
//        });
//        assertTrue(postService.isPostVisibleToUser(post, user));
//        verify(friendService, times(1)).isFriends(anyLong(), anyLong());
//    }
//
//    @Test
//    void isPostVisibleToUser_WhenUserIsCreatorAndPostNotVisible_ReturnsTrue(){
//        post.setVisible(false);
//        assertTrue(postService.isPostVisibleToUser(post, postOwner));
//        verify(friendService, never()).isFriends(anyLong(), anyLong());
//    }

    @Test
    void createPost_WhenNullUser_ThrowsRuntimeException(){
        PostDTORequest postDTORequest = mock(PostDTORequest.class);
        when(postDTORequest.getContent()).thenReturn("Content");
        when(postDTORequest.isVisible()).thenReturn(true);
        when(securityUtil.getAuthenticatedUser()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> postService.createPost(postDTORequest, null));
    }

    @Test
    void createPost_WhenValidUser_ReturnsPostWithCorrectValues(){
        PostDTORequest postDTORequest = mock(PostDTORequest.class);
        when(postDTORequest.getContent()).thenReturn("Content");
        when(postDTORequest.isVisible()).thenReturn(true);
        when(securityUtil.getAuthenticatedUser()).thenReturn(user);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));
        PostDTOResponse post = postService.createPost(postDTORequest, null);
        verify(postRepository, times(1)).save(captor.capture());

        assertEquals(post.getContent(), postDTORequest.getContent());
        assertEquals(post.isVisible(), postDTORequest.isVisible());
        assertEquals(post.getUser().getId(), user.getId());
    }
}
