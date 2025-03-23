package com.example.shortenit.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    private final StringRedisTemplate redisTemplate;

    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void cache(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 7, TimeUnit.DAYS);
    }

    @Override
    public Optional<String> get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value);
    }
}