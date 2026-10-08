package com.bright.gateway.filter;

import com.bright.gateway.service.JWTService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JWTService jwtService;
    private final String ID_HEADER = "X-User-Id";
    private final String EMAIL_HEADER = "X-User-Email";

    public JwtAuthenticationFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (isOpenEndpoint(path)) {
            return chain.filter(exchange);
        }

        HttpCookie jwtCookie = request.getCookies().getFirst("jwt_token");

        if (jwtCookie == null || !jwtService.isTokenValid(jwtCookie.getValue())) {
            return handleUnauthenticated(exchange);
        }

        UUID userId = jwtService.extractId(jwtCookie.getValue());
        String email = jwtService.extractEmail(jwtCookie.getValue());

        ServerHttpRequest modifiedRequest = request.mutate()
                .header(ID_HEADER, userId.toString())
                .header(EMAIL_HEADER, email)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private boolean isOpenEndpoint(String path) {
        return path.equals("/auth/login")
                || path.startsWith("/api/auth/login")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.equals("/favicon.ico");
    }

    private Mono<Void> handleUnauthenticated(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        String location = exchange.getRequest().getURI()
                .resolve("/auth/login")
                .toString();
        response.getHeaders().setLocation(URI.create(location));

        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
