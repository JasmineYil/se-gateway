package com.example.segateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/css/**", "/js/**", "/fonts/**", "img/**").permitAll()
                        .pathMatchers("/", "index.html", "registration.html", "contact.html", "/login.html").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/customers/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/customers/register").permitAll()
                        .pathMatchers(HttpMethod.POST,  "/api/v1/customers/logout").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/auth/check-session").permitAll()
//                        .pathMatchers(HttpMethod.GET, "/api/auth/protected").permitAll()
//                        .pathMatchers(HttpMethod.POST,   "/api/v1/customers/validate-token").permitAll()
//                        .pathMatchers(HttpMethod.GET,  "/api/v1/cars/").permitAll()
//                        .pathMatchers(HttpMethod.POST,  "/api/v1/cars/").permitAll()
//                        .pathMatchers(HttpMethod.GET,  "/api/v1/cars/decode-jwt").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .formLogin(formLogin -> formLogin.loginPage("/login.html"));
        return http.build();
    }
}