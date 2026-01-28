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
import com.magvy.experis.javalava_backend.infrastructure.readonly.ReadOnlyUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ReadOnlyUserRepository userRepository;
    private final FriendService friendService;
    private final int pageSize = 10;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, ReadOnlyUserRepository userRepository, FriendService friendService) {
        this.friendService = friendService;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;

    }
  
    private Post convertToEntity(PostDTORequest postDTORequest, User user) {
        return new Post(
                postDTORequest.getContent(),
                postDTORequest.getPublished(),
                postDTORequest.isVisible(),
                user
        );
    }

    public List<PostDTOResponse> loadPosts(int page, User user) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        if (user == null) {
            return pageToDTOList(postRepository.findByVisibleTrue(pageable));
        }
        return pageToDTOList(postRepository.findPostsForUser(user.getId(), pageable));
    }

    public List<PostDTOResponse> loadPostsByUser(int page, User user, Long selectedId) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        if (user == null) {
            return pageToDTOList(postRepository.findByVisibleTrueAndUserId(selectedId, pageable));
        }
        return pageToDTOList(postRepository.findPostsFromUser(user.getId(), selectedId, pageable));
    }

    public List<PostDTOResponse> loadPostsByFriends(int page, User user) {
        Sort sort = Sort.by("published").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        if (user == null) {
            throw new MissingUserException("Couldn't find user in database");
        }
        return pageToDTOList(postRepository.findPostsFromFriends(user.getId(), pageable));
    }

    private List<PostDTOResponse> pageToDTOList(Page<Post> posts) {
        return posts.stream()
                .map(post -> new PostDTOResponse(
                        post,
                        (int)likeRepository.countByPost(post),
                        (int)commentRepository.countByPost(post)
                ))
                .toList();
    }
  
    public Post findByID(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new MissingResourceException("Post not found", "Post", String.valueOf(postId)));
    }

    public boolean isPostVisibleToUser(Post post, User user) {
        if (post.isVisible()) {
            return true;
        }
        if( user == null) {
            return false;
        }
        if (post.getUser().getId() == user.getId()) {
            return true;
        }
        return friendService.isFriends(user.getId(), post.getUser().getId());
    }

    public Post createPost(User user, PostDTORequest postDTORequest) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot create a post as an anonymous user.");
        }
        Post post = convertToEntity(postDTORequest, user);
        return postRepository.save(post);
    }

    public Optional<Post> getPost(User user, Long id) {
        if (user == null) {
            return postRepository.findByIdAndVisibleTrue(id);
        }
        return postRepository.findByIdIfUserLoggedIn(user, id);
    }

    public Post updatePost(User user, PostDTORequest postDTORequest) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot update a post as an anonymous user.");
        }
        Optional<Post> oPost = postRepository.findById(postDTORequest.getId());
        if (oPost.isEmpty()) {
            throw new MissingPostException("Can't update a missing post.");
        }
        return postRepository.save(oPost.get());
    }

    public void deletePost(User user, Long id) {
        if (user == null) {
            throw new UnauthorizedActionException("Cannot delete a post as an anonymous user.");
        }
        Optional<Post> oPost = postRepository.findById(id);
        if (oPost.isEmpty()) {
            throw new MissingPostException("Can't delete a missing post.");
        }
        Post post = oPost.get();
        if (post.getUser().getId() != user.getId()) {
            throw new UnauthorizedActionException("User does not own this post.");
        }
        postRepository.delete(oPost.get());
    }
}
