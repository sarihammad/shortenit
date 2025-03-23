package com.example.shortenit.service;

import java.util.Optional;

public interface IUrlShorteningService {
    String shortenUrl(String longUrl);
    Optional<String> getLongUrl(String shortUrl);
}