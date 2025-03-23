package com.example.shortenit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;
    private static final int REQUEST_LIMIT = 100;
    private static final int TIME_WINDOW_SECONDS = 60;

    public RateLimitingFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIP = request.getRemoteAddr();
        String key = "rl:" + clientIP;

        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, TIME_WINDOW_SECONDS, TimeUnit.SECONDS);
        }

        if (count != null && count > REQUEST_LIMIT) {
            response.setStatus(429);
            response.getWriter().write("Too many requests - rate limit exceeded");
            return;
        }

        filterChain.doFilter(request, response);
    }
}