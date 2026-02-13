package com.magvy.experis.javalava_backend.infrastructure.seed;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.*;
import com.magvy.experis.javalava_backend.infrastructure.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MessageRepository messageRepository;
    private final LikeRepository likeRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDatabase() {
        return args -> {

            // Prevent reseeding
            if (userRepository.count() > 0) {
                return;
            }

            // ========================
            // USERS
            // ========================

            User john = userRepository.save(
                    new User(null, "john", passwordEncoder.encode("password"))
            );

            User alice = userRepository.save(
                    new User(null, "alice", passwordEncoder.encode("password"))
            );

            User bob = userRepository.save(
                    new User(null, "bob", passwordEncoder.encode("password"))
            );

            User emma = userRepository.save(
                    new User(null, "emma", passwordEncoder.encode("password"))
            );

            // ========================
            // ROLES
            // ========================

            roleRepository.save(createRole(john, RoleEnum.ADMIN));
            roleRepository.save(createRole(alice, RoleEnum.USER));
            roleRepository.save(createRole(bob, RoleEnum.USER));
            roleRepository.save(createRole(emma, RoleEnum.USER));

            // ========================
            // FRIENDS
            // ========================

            friendRepository.save(new Friend(john, alice));
            friendRepository.save(new Friend(john, bob));
            friendRepository.save(new Friend(alice, emma));

            // ========================
            // POSTS
            // ========================

            Post post1 = postRepository.save(
                    new Post(null, "Hello world! 🚀",
                            LocalDateTime.now().minusDays(2),
                            true, john)
            );

            Post post2 = postRepository.save(
                    new Post(null, "Spring Boot is awesome!",
                            LocalDateTime.now().minusDays(1),
                            true, alice)
            );

            Post post3 = postRepository.save(
                    new Post(null, "Working on my backend project.",
                            LocalDateTime.now().minusHours(5),
                            true, bob)
            );

            // ========================
            // COMMENTS
            // ========================

            commentRepository.save(
                    new Comment("Nice post!", LocalDateTime.now().minusDays(1), post1, alice)
            );

            commentRepository.save(
                    new Comment("Totally agree!", LocalDateTime.now().minusHours(10), post2, john)
            );

            commentRepository.save(
                    new Comment("Good luck 💪", LocalDateTime.now().minusHours(3), post3, emma)
            );

            // ========================
            // LIKES
            // ========================

            likeRepository.save(new Like(alice, post1));
            likeRepository.save(new Like(bob, post1));
            likeRepository.save(new Like(john, post2));
            likeRepository.save(new Like(emma, post2));
            likeRepository.save(new Like(john, post3));

            // ========================
            // MESSAGES
            // ========================

            messageRepository.save(
                    new Message("Hey Alice!", LocalDateTime.now().minusDays(1), john, alice)
            );

            messageRepository.save(
                    new Message("Hi John 😊", LocalDateTime.now().minusHours(20), alice, john)
            );

            messageRepository.save(
                    new Message("Are you joining the project?", LocalDateTime.now().minusHours(2), bob, emma)
            );

            messageRepository.save(
                    new Message("Yes! Starting today.", LocalDateTime.now().minusMinutes(30), emma, bob)
            );

            System.out.println("Database seeded successfully!");
        };
    }

    private Role createRole(User user, RoleEnum roleEnum) {
        return new Role(user, roleEnum);
    }
}
