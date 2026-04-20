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
                        auth -> auth
                                // Public endpoints
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/test").permitAll()

                                // Loan endpoints
                                .requestMatchers("/loan/borrow").hasRole("READER")
                                .requestMatchers("/loan/request-return/**").hasRole("READER")
                                .requestMatchers("/loan/approve/**").hasRole("LIBRARIAN")
                                .requestMatchers("/loan/approve-return/**").hasRole("LIBRARIAN")
                                .requestMatchers("/loan/getAll").hasAnyRole("LIBRARIAN", "ADMIN")
                                .requestMatchers("/loan/user/**").hasAnyRole("READER", "LIBRARIAN", "ADMIN")

                                // User management
                                .requestMatchers("/user/**").hasAnyRole("ADMIN", "LIBRARIAN")

                                // Book endpoints
                                .requestMatchers("/book/add", "/book/update/**", "/book/delete/**").hasAnyRole("LIBRARIAN", "ADMIN")
                                .requestMatchers("/book/getAll", "/book/search/**", "/book/**").hasAnyRole("READER", "LIBRARIAN", "ADMIN")
                )
                .sessionManagement(session -> session
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

