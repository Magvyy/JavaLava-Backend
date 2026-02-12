package com.magvy.experis.springboot_demo.integrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magvy.experis.javalava_backend.App;
import com.magvy.experis.javalava_backend.application.DTOs.incoming.AuthDTO;
import com.magvy.experis.javalava_backend.controllers.AuthController;
import com.magvy.experis.javalava_backend.controllers.PostController;
import com.magvy.experis.javalava_backend.domain.services.WebSocketService;
import org.jspecify.annotations.NonNull;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureWebTestClient
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class AuthorizationIntegrationTest {

   @Autowired
   private AuthController authController;

   @Autowired
   private PostController postController;

   @Autowired
   private WebTestClient webTestClient;

   @Autowired
   private WebSocketService webSocketService;

   @LocalServerPort
   private Integer port;

   @Test
   void contextLoads(){
       assertThat(authController).isNotNull();
       assertThat(postController).isNotNull();
       assertThat(webTestClient).isNotNull();
   }

    @Test
    void loginAsAdmin_SuccessfullyDeleteOtherUserPost() throws JsonProcessingException {
        // 1 Register regular user
        AuthDTO authDTO = new AuthDTO("testUser2", "password");

        MultiValueMap<String, ResponseCookie> result = webTestClient.post().uri("/auth/register")
                .bodyValue(authDTO)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult().getResponseCookies();

        // 2 create post as regular user
        EntityExchangeResult<String> post = webTestClient.post().uri("/post")
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
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(post.getResponseBody());
        int post_id = jsonNode.get("id").asInt();

        //3 Read post as regular user
        webTestClient.get().uri(String.format("/post/%s", post_id))
            .cookie("access_token", result.get("access_token").getFirst().getValue())
            .exchange()
            .expectStatus().is2xxSuccessful();

        // 4 Login as admin
        AuthDTO adminAuthDTO = new AuthDTO("admin", "admin");

        MultiValueMap<String, ResponseCookie> adminResult = webTestClient.post().uri("/auth/login")
                .bodyValue(adminAuthDTO)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .returnResult().getResponseCookies();

//        // 5 Delete post as admin
        webTestClient.delete().uri(String.format("/post/%s", post_id))
                .cookie("access_token", adminResult.get("access_token").getFirst().getValue())
                .exchange()
                .expectStatus().is2xxSuccessful();

        // 6 Verify post deleted
        webTestClient.get().uri(String.format("/post/%s", post_id))
                .cookie("access_token", result.get("access_token").getFirst().getValue())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void loginAsUser_UnSuccessfullyDeleteOtherUserPost() throws JsonProcessingException {

       // Register user to create the post
        AuthDTO userDTO = new AuthDTO("kfdlsajfk", "password");
        MultiValueMap<String, ResponseCookie> userCookieResult = webTestClient.post().uri("/auth/register")
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult().getResponseCookies();

        // Create the post
        EntityExchangeResult<String> post = webTestClient.post().uri("/post")
                .cookie("access_token", userCookieResult.get("access_token").getFirst().getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                  {
                    "content": "This is a post content",
                    "published": "09-02-2026 14:30:00",
                     "visible": true
                  }
                """)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(post.getResponseBody());
        int post_id = jsonNode.get("id").asInt();

        // Ensure post is created
        webTestClient.get().uri(String.format("/post/%s", post_id))
                .cookie("access_token", userCookieResult.get("access_token").getFirst().getValue())
                .exchange()
                .expectStatus().is2xxSuccessful();

        // Create new user to attempt delete
        AuthDTO otherUserDTO = new AuthDTO("jklsdklj", "password");
        MultiValueMap<String, ResponseCookie> otherUserCookieResult = webTestClient.post().uri("/auth/register")
                .bodyValue(otherUserDTO)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult().getResponseCookies();

//        // Try delete post as other user
        webTestClient.delete().uri(String.format("/post/%s", post_id))
                .cookie("access_token", otherUserCookieResult.get("access_token").getFirst().getValue())
                .exchange()
                .expectStatus().is4xxClientError();

        // 6 Verify post not deleted
        webTestClient.get().uri(String.format("/post/%s", post_id))
                .cookie("access_token", userCookieResult.get("access_token").getFirst().getValue())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void loginAsRegularUserConnectToWebsocketServerAndReceiveMessage() throws ExecutionException, InterruptedException, TimeoutException {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        CountDownLatch connectionLatch = new CountDownLatch(1);
        CountDownLatch messageReceivedLatch = new CountDownLatch(1);

       //1 Register regular user
        String url = "ws://localhost:" + port + "/websocket";
        AuthDTO authDTO = new AuthDTO("testUser", "password");
        MultiValueMap<String, ResponseCookie> result = webTestClient.post().uri("/auth/register")
                .bodyValue(authDTO)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult().getResponseCookies();

        String userCookie = result.get("access_token").getFirst().getValue();

        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new StringMessageConverter());

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.add("Cookie", "access_token="+ userCookie);

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("Cookie", "access_token="+ userCookie);


        // 2 SessionHandler to handle connection and messages from backend
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, @NonNull StompHeaders connectedHeaders) {
                System.out.println("WebSocket connection established");
                connectionLatch.countDown();

                session.subscribe("/user/queue/notifications", this);
            }
            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {
                String message = payload != null ? payload.toString() : "null";
                System.out.println("Received message: " + message);
                boolean offered = blockingQueue.offer(message);
                if (offered) {
                    messageReceivedLatch.countDown();
                }
            }
            @Override
            public void handleTransportError(@NonNull StompSession session, Throwable exception) {
                System.err.println("WebSocket transport error: " + exception.getMessage());
                messageReceivedLatch.countDown();
                connectionLatch.countDown();
            }
            @Override
            public void handleException(@NonNull StompSession session, StompCommand command, @NonNull StompHeaders headers, byte @NonNull [] payload, Throwable exception) {
                System.err.println("WebSocket error: " + exception.getMessage());
                messageReceivedLatch.countDown();
                connectionLatch.countDown();
            }
        };

        // 3 Connect to WebSocket server
        StompSession session = stompClient.connectAsync(url, webSocketHttpHeaders, stompHeaders, sessionHandler).get(5, SECONDS);
        boolean isConnected = connectionLatch.await(5, SECONDS);
        assertThat(isConnected).as("WebSocket connection established").isTrue();

        webSocketService.sendNotification("testUser", "Hello from test!");
        boolean messageReceived = messageReceivedLatch.await(5, SECONDS);
        assertThat(messageReceived).as("Received message: Hello from test!").isTrue();

        String receivedMessage = blockingQueue.poll(5, SECONDS);
        assertThat(receivedMessage).as("Received message content").isEqualTo("Hello from test!");

    }
}
