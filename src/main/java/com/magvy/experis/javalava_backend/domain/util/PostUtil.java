package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PostDTOResponse;
import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.PostException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostUtil {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final SecurityUtil securityUtil;
    private final FriendUtil friendUtil;
    private final AttachmentUtil attachmentUtil;

    public PostUtil(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, SecurityUtil securityUtil, FriendUtil friendUtil, AttachmentUtil attachmentUtil) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.securityUtil = securityUtil;
        this.friendUtil = friendUtil;
        this.attachmentUtil = attachmentUtil;
    }

    public Post convertToEntity(PostDTORequest postDTORequest, Long attachmentId) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        return new Post(
                postDTORequest.getContent(),
                postDTORequest.isVisible(),
                authenticatedUser,
                attachmentId
        );
    }

    public List<PostDTOResponse> pageToDTOList(Page<Post> posts, Long id) {
        return posts.stream()
                .map(post -> new PostDTOResponse(
                        post,
                        likeRepository.existsByPostIdAndUserId(post.getId(), id),
                        (int) likeRepository.countByPostId(post.getId()),
                        (int) commentRepository.countByPost(post)
                ))
                .toList();
    }

    public Post findByIdOrThrow(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new PostException("Post not found", HttpStatus.NOT_FOUND));
    }

    public boolean isPostVisibleToAuthenticatedUser(Post post) {
        if (post.isVisible()) {
            return true;
        }
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return false;
        }
        if (authenticatedUserOwnsPost(post)) {
            return true;
        }
        return friendUtil.isFriends(post.getUser().getId());
    }

    public void validate(PostDTORequest postDTORequest) {
        if (!isValidContent(postDTORequest.getContent())) throw new PostException("Invalid content", HttpStatus.BAD_REQUEST);
    }

    private boolean isValidContent(String content) {
        return !content.trim().isEmpty();
    }

    public boolean authenticatedUserOwnsPost(Post post) {
        User authenticatedUser = securityUtil.getAuthenticatedUser();
        if (authenticatedUser == null) return false;
        return authenticatedUser.getId().equals(post.getUser().getId());
    }
}
