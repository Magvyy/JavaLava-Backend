package com.magvy.experis.javalava_backend.application.security.config;
import com.magvy.experis.javalava_backend.application.security.filter.util.JwtUtil;
import com.magvy.experis.javalava_backend.domain.exceptions.UnauthorizedActionException;
import io.jsonwebtoken.JwtException;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private static final String JWT_COOKIE_NAME = "access_token";

    public WebSocketConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor == null) {
                    throw new UnauthorizedActionException("Invalid STOMP message");
                }

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    try {
                        String token = extractToken(accessor);

                        if (token == null || token.isEmpty()) {
                            throw new UnauthorizedActionException("Missing JWT token");
                        }

                        if (!jwtUtil.validateToken(token)) {
                            throw new UnauthorizedActionException("Invalid or expired JWT token");
                        }

                        String username = jwtUtil.extractUsername(token);
                        System.out.println("WebSocket connection attempt with username: " + username);

                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        accessor.setUser(authenticationToken);

                    } catch (JwtException e) {
                        throw new UnauthorizedActionException("JWT parsing error: " + e.getMessage());
                    } catch (UsernameNotFoundException e) {
                        throw new UnauthorizedActionException("User not found: " + e.getMessage());
                    }
                }
                return message;
            }

        });

    }

    private String extractToken(StompHeaderAccessor accessor) {
        String cookieHeader = accessor.getFirstNativeHeader("Cookie");
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            return extractTokenFromCookie(cookieHeader);
        }
        return null;
    }


    private String extractTokenFromCookie(String cookieHeader) {
        if (cookieHeader == null || cookieHeader.isEmpty()) {
            return null;
        }

        String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            cookie = cookie.trim();
            if (cookie.startsWith(JWT_COOKIE_NAME + "=")) {
                String token = cookie.substring(JWT_COOKIE_NAME.length() + 1);
                if (!token.isEmpty()) {
                    return token;
                }
            }
        }
        return null;
    }

}
