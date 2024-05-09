package com.example.segateway.config;

import com.example.segateway.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.COOKIE);
        String jwtTokenPrefix = "jwtToken=";

        logger.info("Received request: {}", exchange.getRequest().getURI());
        logger.debug("Received cookies: {}", authHeader);

        if (authHeader != null) {
            String[] cookies = authHeader.split("; ");
            String jwt = Arrays.stream(cookies)
                    .filter(cookie -> cookie.startsWith(jwtTokenPrefix))
                    .map(cookie -> cookie.substring(jwtTokenPrefix.length()))
                    .findFirst()
                    .orElse(null);

            logger.debug("Extracted JWT token: {}", jwt);

            if (jwt != null && jwtService.validateToken(jwt)) {
                String username = jwtService.extractUsername(jwt);
                logger.info("Valid JWT token found for user: {}", username);

                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, List.of()); // No authorities
                SecurityContext context = new SecurityContextImpl(auth);
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
            }else {
                logger.warn("Invalid or missing JWT token");
            }
        } else {
            logger.warn("No cookies found in the request");
        }
        return chain.filter(exchange);
    }
}