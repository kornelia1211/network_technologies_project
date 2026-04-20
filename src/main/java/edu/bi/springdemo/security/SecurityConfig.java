package edu.bi.springdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenService jwtTokenService;

    @Autowired
    public SecurityConfig(JwtTokenService jwtTokenService){
        this.jwtTokenService = jwtTokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new JWTTokenFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry ->
                                authorizationManagerRequestMatcherRegistry
                                        .requestMatchers("/login").permitAll()
                                        .requestMatchers("/test").permitAll()

                                        .requestMatchers("/loan/getAll").hasAnyRole("LIBRARIAN", "ADMIN")
                                        .requestMatchers("/loan/user/**").hasAnyRole("READER", "LIBRARIAN", "ADMIN")
                                        .requestMatchers("/loan/borrow").hasAnyRole("READER")
                                        .requestMatchers("/loan/return/**").hasAnyRole("READER")

                                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "LIBRARIAN")

                                        .requestMatchers("/book/add", "/book/{id}").hasAnyRole("LIBRARIAN", "ADMIN")
                                        .requestMatchers("/book/getAll", "/book/search/title", "/book/search/author").hasAnyRole("READER", "LIBRARIAN", "ADMIN")
                ).sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}

//admin
//{
//  "username": "admin",
//  "password": "admin123"
//}

//reader
//{
//  "username": "reader1",
//  "password": "password123"
//}

//librarian
//{
//  "username": "librarian1",
//  "password": "lib123",
//}

