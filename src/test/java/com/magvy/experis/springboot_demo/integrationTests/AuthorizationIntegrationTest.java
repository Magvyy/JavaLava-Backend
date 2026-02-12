package com.magvy.experis.springboot_demo.integrationTests;

import com.magvy.experis.javalava_backend.App;
import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.controllers.AuthController;
import com.magvy.experis.javalava_backend.controllers.PostController;
import io.swagger.v3.core.util.Json;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureWebTestClient
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class AuthorizationIntegrationTest {


    // 1. Create user with registerMapping
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

    @Test
    void loginAsAdmin_SuccessfullyDeleteOtherUserPost()  {
        // 1 Register regular user
        AuthDTO authDTO = new AuthDTO("testUser", "password");

        MultiValueMap<String, ResponseCookie> result = webTestClient.post().uri("/auth/register")
                .bodyValue(authDTO)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult().getResponseCookies();

        // 2 create post as regular user
        webTestClient.post().uri("/post")
                .cookie("access_token", result.get("access_token").getFirst().getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                  {
                    "content": "This is a post content",
                    "published": "09-02-2026 14:30:00",
                     "visible": true
                  }
                """)
                .exchange()
                .expectStatus().is2xxSuccessful();

        //3 Read post as regular user
        EntityExchangeResult<String> post =  webTestClient.get().uri("/post/1")
            .cookie("access_token", result.get("access_token").getFirst().getValue())
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(String.class)
            .returnResult();

        // 4 Login as admin
        AuthDTO adminAuthDTO = new AuthDTO("admin", "admin");

        MultiValueMap<String, ResponseCookie> adminResult = webTestClient.post().uri("/auth/login")
                .bodyValue(adminAuthDTO)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .returnResult().getResponseCookies();

        // 5 Delete post as admin
        webTestClient.delete().uri("/post/1")
                .cookie("access_token", adminResult.get("access_token").getFirst().getValue())
                .exchange()
                .expectStatus().is2xxSuccessful();

        // 6 Verify post deleted
        webTestClient.get().uri("/post/1")
                .cookie("access_token", result.get("access_token").getFirst().getValue())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .returnResult();
    }
}
