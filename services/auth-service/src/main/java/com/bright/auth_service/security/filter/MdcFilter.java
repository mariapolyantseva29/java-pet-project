package com.bright.auth_service.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MdcFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");
        String traceId = request.getHeader("X-Trace-Id");

        try {
            MDC.put("userId", userId);
            MDC.put("traceId", traceId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
