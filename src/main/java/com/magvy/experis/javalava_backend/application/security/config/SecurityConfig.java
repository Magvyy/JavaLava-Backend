package com.magvy.experis.javalava_backend.application.security.config;

import com.magvy.experis.javalava_backend.application.security.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final String jwt_cookie_name;

    public SecurityConfig(JwtFilter jwtFilter, CustomAuthenticationProvider customAuthenticationProvider, @Value("${jwt.name}") String jwt_cookie_name) {
        this.jwtFilter = jwtFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.jwt_cookie_name = jwt_cookie_name;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequests ->
                    authorizeHttpRequests
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/swagger-resources/**").permitAll()
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/post/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/post/friends/**").authenticated()
                            .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                            .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .deleteCookies(jwt_cookie_name)
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }
}