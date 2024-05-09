package com.example.segateway.controller;

import com.example.segateway.service.AuthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class GatewayController {

    private final AuthClientService authClientService;
    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    public GatewayController(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    @GetMapping("/protected")
    public Mono<ResponseEntity<String>> accessProtectedResource(@RequestHeader("Cookie") String token) {
        String jwtToken = token.startsWith("jwtToken=") ? token.substring(9) : token;

        return authClientService.validateToken(jwtToken)
                .map(isValid -> {
                    if (isValid) {
                        return ResponseEntity.ok("Access to protected resource granted.");
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token");
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No token provided"));
    }

    @GetMapping("/check-session")
    public Mono<ResponseEntity<String>> checkCustomerSession(ServerWebExchange exchange) {
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        HttpCookie jwtTokenCookie = cookies.getFirst("jwtToken");

        if (jwtTokenCookie != null) {
            String cookieValue = jwtTokenCookie.getValue();
            logger.info("1 Cookie GATEWAY: {}", cookieValue);
            return authClientService.checkSession(cookieValue)
                    .map(response -> ResponseEntity.ok().body(response))
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session is not valid."));
        } else {
            logger.info("No 'jwtToken' cookie found");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided"));
        }
    }
}
