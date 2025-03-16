package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;


public class ShortenUrlRequest {
    
    @NotBlank(message = "Long URL cannot be empty")
    private String longUrl;

    public ShortenUrlRequest() {}

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
