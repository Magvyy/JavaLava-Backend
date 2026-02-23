package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PostDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.PostException;
import com.magvy.experis.javalava_backend.domain.services.FriendService;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.MissingResourceException;

@Component
public class PostUtil {
    private final SecurityUtil securityUtil;
    private final PostRepository postRepository;;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final FriendService friendService;

    public PostUtil(SecurityUtil securityUtil, PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, FriendService friendService) {
        this.securityUtil = securityUtil;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.friendService = friendService;
    }

    public Post convertToEntity(PostDTORequest postDTORequest, User user) {
        return new Post(
                postDTORequest.getContent(),
                postDTORequest.isVisible(),
                user
        );
    }

    public List<PostDTOResponse> pageToDTOList(Page<Post> posts, Long id) {
        return posts.stream()
                .map(post -> new PostDTOResponse(
                        post,
                        likeRepository.existsByPost_idAndUser_Id(post.getId(), id),
                        (int) likeRepository.countByPost_Id(post.getId()),
                        (int) commentRepository.countByPost(post)
                ))
                .toList();
    }

    public Post findByIdOrThrow(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new PostException("Post not found", HttpStatus.NOT_FOUND));
    }

    public boolean isPostVisibleToUser(Post post, User user) {
        if (post.isVisible()) {
            return true;
        }
        if( user == null) {
            return false;
        }
        if (post.getUser().getId().equals(user.getId())) {
            return true;
        }
        return friendService.isFriends(user.getId(), post.getUser().getId());
    }

    public boolean isValidContent(String content) {
        return !content.trim().isEmpty();
    }

    public boolean authenticatedUserOwnsPost(Post post) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        return authenticatedUser.getId().equals(post.getUser().getId());
    }
}
