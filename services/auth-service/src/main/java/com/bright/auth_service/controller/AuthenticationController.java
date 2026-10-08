package com.bright.auth_service.controller;

import com.bright.auth_service.dto.LoginRequest;
import com.bright.auth_service.security.AppUser;
import com.bright.auth_service.security.AppUserDetailsService;
import com.bright.auth_service.service.JWTService;
import com.bright.auth_service.service.KafkaLogProducer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final JWTService jwtService;
    private final AppUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final KafkaLogProducer kafkaLogProducer;

    public AuthenticationController(
            AppUserDetailsService userDetailsService,
            AuthenticationManager authenticationManager,
            JWTService jwtService,
            KafkaLogProducer kafkaLogProducer
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.kafkaLogProducer = kafkaLogProducer;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        kafkaLogProducer.sendLog("INFO", "Received login method");
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        AppUser appUser = null;

        if (!userDetailsService.existsByEmail(email)) {
            appUser = userDetailsService.createUser(email, password);
        } else {
            appUser = (AppUser) userDetailsService.loadUserByUsername(email);
        }
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        String jwt = jwtService.create(appUser.getId(), email);
        Cookie cookie = new Cookie("jwt_token", jwt);

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
