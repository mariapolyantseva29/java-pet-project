package com.bright.gateway.filter;

import com.bright.gateway.service.BucketService;
import com.bright.gateway.service.JWTService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {
    private final BucketService bucketService;
    private final JWTService jwtService;

    public RateLimitFilter(BucketService bucketService, JWTService jwtService) {
        this.bucketService = bucketService;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = null;
        var cookies = exchange.getRequest().getCookies();

        if (cookies != null && cookies.containsKey("jwt_token")) {
            token = Objects.requireNonNull(cookies.getFirst("jwt_token")).getValue();
        }

        if (token == null) {
            return chain.filter(exchange);
        }

        UUID id = jwtService.extractId(token);

        if (!bucketService.exist(id)) {
            bucketService.create(id);
        } else {
            if (!bucketService.availableRequest(id)) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
