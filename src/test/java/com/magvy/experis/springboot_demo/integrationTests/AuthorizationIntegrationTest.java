package com.magvy.experis.springboot_demo.integrationTests;

import com.magvy.experis.javalava_backend.App;
import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.controllers.AuthController;
import com.magvy.experis.javalava_backend.controllers.PostController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureWebTestClient
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class AuthorizationIntegrationTest {


    // 1. Create user with reggisterMapping
    // 2. Login user with login endpoint
    // 3. Create post with /post endpoint
    // 3.1 Verify post created
    // 4. Login as admin
    // 5. Call delete at endpoint post/{userId}
    // 6. Verify post deleted.


   @Autowired
   private AuthController authController;

   @Autowired
   private PostController postController;

   @Autowired
   private WebTestClient webTestClient;

   @Test
   void contextLoads(){
       assertThat(authController).isNotNull();
       assertThat(postController).isNotNull();
       assertThat(webTestClient).isNotNull();
   }

    @BeforeEach
    void setup(){

    }


    @Test
    void loginAsAdmin_SuccessfullyDeleteOtherUserPost(){
        // 1
        AuthDTO authDTO = new AuthDTO("testUser", "password");
        webTestClient.post().uri("/auth/register")
                .bodyValue(authDTO)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
