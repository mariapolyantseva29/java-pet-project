package com.bright.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WebConfig implements WebFluxConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public RouterFunction<ServerResponse> htmlRouter() {
        return RouterFunctions.route()
                .GET("/auth/login", request ->
                        ServerResponse.ok().bodyValue(new ClassPathResource("static/login-page.html")))
                .GET("/situations", request ->
                        ServerResponse.ok().bodyValue(new ClassPathResource("static/situations.html")))
                .GET("/situations/edited", request ->
                        ServerResponse.ok().bodyValue(new ClassPathResource("static/situations.html")))
                .GET("/situations/done", request ->
                        ServerResponse.ok().bodyValue(new ClassPathResource("static/situations.html")))
                .GET("/situations/new", request ->
                        ServerResponse.ok().bodyValue(new ClassPathResource("static/newSituation.html")))
                .GET("/situations/{id}", request -> {
                    String id = request.pathVariable("id");

                    return ServerResponse.ok()
                            .header("Content-Type", "text/html; charset=UTF-8")
                            .body(
                                    BodyInserters.fromResource(new ClassPathResource("static/situation.html")));
                })
                .build();
    }
}
