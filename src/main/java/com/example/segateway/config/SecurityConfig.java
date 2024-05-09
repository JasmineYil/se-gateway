package com.example.segateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                        .pathMatchers("/", "index.html", "registration.html", "contact.html", "login.html").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/customers/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/customers/register").permitAll()
                        .pathMatchers(HttpMethod.POST,  "/api/v1/customers/logout").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/auth/check-session").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .cors().and()
                .formLogin(formLogin -> formLogin.loginPage("/login.html"));
        return http.build();
    }

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:9095"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Cookie", "Content-Type"));
        corsConfig.setExposedHeaders(List.of("Cookie"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }
}