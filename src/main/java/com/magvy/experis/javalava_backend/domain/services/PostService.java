package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.PostDTOResponse;
import com.magvy.experis.javalava_backend.application.DTOs.incoming.PostDTORequest;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingPostException;
import com.magvy.experis.javalava_backend.domain.exceptions.MissingUserException;
import com.magvy.experis.javalava_backend.domain.exceptions.UnauthorizedActionException;
import com.magvy.experis.javalava_backend.infrastructure.repositories.CommentRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.LikeRepository;
import com.magvy.experis.javalava_backend.infrastructure.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final FriendService friendService;
    private final int pageSize = 10;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, FriendService friendService) {
        this.friendService = friendService;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;

    }
  
    private Post convertToEntity(Long id, PostDTORequest postDTORequest, User user) {
        return new Post(
                id,
                postDTORequest.getContent(),
                postDTORequest.getPublished(),
                postDTORequest.isVisible(),
                user
        );
    }

    public List<PostDTOResponse> loadPosts(int page, Optional<User> user) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        if (user.isEmpty()) {
            return pageToDTOList(postRepository.findByVisibleTrue(pageable), (long) -1);
        }
        return pageToDTOList(postRepository.findPostsForUser(user.get().getId(), pageable), user.get().getId());
    }

    public List<PostDTOResponse> loadPostsByUser(int page, Optional<User> user, Long selectedId) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        if (user.isEmpty()) {
            return pageToDTOList(postRepository.findByVisibleTrueAndUserId(selectedId, pageable), (long) -1);
        }
        return pageToDTOList(postRepository.findPostsFromUser(user.get().getId(), selectedId, pageable), user.get().getId());
    }

    public List<PostDTOResponse> loadPostsByFriends(int page, User user) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        if (user == null) {
            throw new MissingUserException("Couldn't find user in database");
        }
        return pageToDTOList(postRepository.findPostsFromFriends(user.getId(), pageable), user.getId());
    }

    private List<PostDTOResponse> pageToDTOList(Page<Post> posts, Long id) {
        return posts.stream()
                .map(post -> new PostDTOResponse(
                        post,
                        likeRepository.existsByPost_idAndUser_Id(post.getId(), id),
                        (int)likeRepository.countByPost_Id(post.getId()),
                        (int)commentRepository.countByPost(post)
                ))
                .toList();
    }
  
    public Post findByID(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new MissingResourceException("Post not found", "Post", String.valueOf(id)));
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

    public Post createPost(User user, PostDTORequest postDTORequest) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot create a post as an anonymous user.");
        }
        Post post = convertToEntity(null, postDTORequest, user);
        return postRepository.save(post);
    }

    public Optional<Post> getPost(Long id, Optional<User> user) {
        if (user.isEmpty()) {
            return postRepository.findByIdAndVisibleTrue(id);
        }
        return postRepository.findByIdIfUserLoggedIn(user.get(), id);
    }

    public Post updatePost(Long id, User user, PostDTORequest postDTORequest) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot update a post as an anonymous user.");
        }
        Optional<Post> oPost = postRepository.findById(id);
        if (oPost.isEmpty()) {
            throw new MissingPostException("Can't update a missing post.");
        }
        Post post = oPost.get();
        if (!post.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("User does not own this post.");
        }
        post = convertToEntity(id, postDTORequest, user);
        return postRepository.save(post);
    }

    public void deletePost(Long id, User user) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot delete a post as an anonymous user.");
        }
        Optional<Post> oPost = postRepository.findById(id);
        if (oPost.isEmpty()) {
            throw new MissingPostException("Can't delete a missing post.");
        }
        Post post = oPost.get();
        if (!post.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("User does not own this post.");
        }
        postRepository.delete(post);
    }
}
