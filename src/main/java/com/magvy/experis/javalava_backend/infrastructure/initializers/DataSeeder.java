package com.magvy.experis.javalava_backend.infrastructure.initializers;

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
import java.util.*;

@Configuration
@Profile({"dev", "local"})
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MessageRepository messageRepository;
    private final LikeRepository likeRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDatabase() {
        System.out.println("Seeding database...");
        return args -> {

            if (!userRepository.findAll().isEmpty()) return;

            // Create admin
            User admin = new User(
                    "admin",
                    passwordEncoder.encode("admin")
            );
            admin.addRole(RoleEnum.ADMIN);
            userRepository.save(admin);

            // ========================
            // USERS (15 regular users)
            // ========================
            List<String> usernames = Arrays.asList(
                    "john", "alice", "bob", "emma", "oliver",
                    "sophia", "liam", "mia", "noah", "ava",
                    "lucas", "isabella", "mason", "charlotte", "ethan"
            );

            List<User> users = new ArrayList<>();
            for (String username : usernames) {
                User u = new User(username, passwordEncoder.encode("password"));
                userRepository.save(u);
                users.add(u);
            }

            // ========================
            // POSTS (~30)
            // ========================
            String[] samplePosts = new String[]{
                    "Hello world! 🚀",
                    "Spring Boot is awesome!",
                    "Working on my backend project.",
                    "Coffee and code.",
                    "Just pushed a new feature.",
                    "Reading a good book this weekend.",
                    "Anyone up for pair programming?",
                    "Java streams are powerful.",
                    "Refactoring some old code today.",
                    "Unit tests saved me again.",
                    "Deploying to staging.",
                    "CI/CD pipeline work.",
                    "Trying out a new library.",
                    "Debugging a tricky bug.",
                    "Weekend hackathon vibes.",
                    "Learning more about security.",
                    "Optimizing database queries.",
                    "Happy to help on PR reviews.",
                    "Sharing some developer tips.",
                    "Exploring microservices patterns.",
                    "Working remotely today.",
                    "Good morning!",
                    "Creating a small side project.",
                    "GraphQL vs REST discussion.",
                    "Pairing with a teammate.",
                    "Setting up integration tests.",
                    "Refreshed the README.",
                    "Writing documentation.",
                    "Learning Kotlin this month.",
                    "Celebrating a release! 🎉"
            };

            Random rand = new Random(12345); // deterministic
            List<Post> posts = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                String content = samplePosts[i % samplePosts.length];
                User owner = users.get(rand.nextInt(users.size()));
                LocalDateTime time = LocalDateTime.now().minusHours(rand.nextInt(240)); // within 10 days
                Post p = new Post(null, content, time, true, owner);
                postRepository.save(p);
                posts.add(p);
            }

            // ========================
            // FRIENDS (random pairs)
            // ========================
            Set<String> friendPairs = new HashSet<>();
            int desiredFriendships = 25;
            for (int i = 0; i < desiredFriendships; i++) {
                User a = users.get(rand.nextInt(users.size()));
                User b = users.get(rand.nextInt(users.size()));
                if (a.getId() == null || b.getId() == null) continue; // safety
                if (a.equals(b)) continue;
                String key = a.getId() < b.getId() ? a.getId() + ":" + b.getId() : b.getId() + ":" + a.getId();
                if (friendPairs.contains(key)) continue;
                friendPairs.add(key);
                friendRepository.save(new Friend(a, b));
            }

            // ========================
            // COMMENTS (random)
            // ========================
            String[] sampleComments = new String[]{
                    "Nice post!",
                    "Totally agree!",
                    "Good luck 💪",
                    "Amazing work!",
                    "Thanks for sharing.",
                    "Helpful, thanks!",
                    "Love this.",
                    "Interesting take.",
                    "Can you share more details?",
                    "Great explanation!"
            };

            int commentCount = 45;
            for (int i = 0; i < commentCount; i++) {
                Post target = posts.get(rand.nextInt(posts.size()));
                User author = users.get(rand.nextInt(users.size()));
                String text = sampleComments[rand.nextInt(sampleComments.length)];
                LocalDateTime time = target.getPublished() != null ?
                        target.getPublished().plusHours(rand.nextInt(48)) :
                        LocalDateTime.now().minusHours(rand.nextInt(200));
                commentRepository.save(new Comment(text, time, target, author));
            }

            // ========================
            // LIKES (random unique per post)
            // ========================
            for (Post p : posts) {
                int likesForPost = 1 + rand.nextInt(6); // 1..6 likes
                Set<Long> likedBy = new HashSet<>();
                for (int k = 0; k < likesForPost; k++) {
                    User liker = users.get(rand.nextInt(users.size()));
                    if (liker.getId() == null) continue;
                    if (likedBy.contains(liker.getId())) continue;
                    likedBy.add(liker.getId());
                    likeRepository.save(new Like(liker, p));
                }
            }

            // ========================
            // MESSAGES (random conversations)
            // ========================
            int messagesToCreate = 40;
            for (int i = 0; i < messagesToCreate; i++) {
                User sender = users.get(rand.nextInt(users.size()));
                User receiver = users.get(rand.nextInt(users.size()));
                if (sender.equals(receiver)) continue;
                String content = (i % 5 == 0) ? "Are you joining the project?" : "Hey, let's catch up later.";
                LocalDateTime time = LocalDateTime.now().minusHours(rand.nextInt(300));
                messageRepository.save(new Message(content, time, sender, receiver));
            }

            System.out.println("Database seeded successfully with " + users.size() + " users and " + posts.size() + " posts.");
        };
    }
}
