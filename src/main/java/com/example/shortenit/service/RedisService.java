package com.example.shortenit.service;

import java.util.Optional;

public interface RedisService {
    void cache(String key, String value);
    Optional<String> get(String key);
}