package com.example.shortenit.kafka;

import lombok.Data;

@Data
public class ShortenedUrlEvent {
    private String shortUrl;
    private String longUrl;

    public ShortenedUrlEvent(String shortUrl, String longUrl) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }
}