package com.example.segateway.config;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtPropagationFilter implements WebFilter {

    //TODO: NOCH NICHT GETESTET! (War nicht notwendig) FUNKTION: JwtToken an Microservices weiterleiten.
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.COOKIE))
                .filter(authHeader -> authHeader.startsWith("jwtToken="))
                .map(authHeader -> exchange.mutate().request(exchange.getRequest().mutate()
                        .header("Authorization", authHeader).build()).build()) //Todo: Hier eventuell Cookie statt Authorization
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }
}