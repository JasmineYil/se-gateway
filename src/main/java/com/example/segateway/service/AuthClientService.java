package com.example.segateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthClientService {

    private final WebClient webClient;

    public AuthClientService(@Value("http://localhost:9092") String authServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(authServiceUrl)
                .build();
    }

    public Mono<Boolean> validateToken(String token) {
        return webClient.post()
                .uri("/api/v1/customers/validate-token")
                .bodyValue(token)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<String> checkSession(String cookie) {
        return webClient.get()
                .uri("/api/v1/customers/check-session" )
                .header(HttpHeaders.COOKIE, "jwtToken=" + cookie)
                .retrieve()
                .bodyToMono(String.class);
    }
}
