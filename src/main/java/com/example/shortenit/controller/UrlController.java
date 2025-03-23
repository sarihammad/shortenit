package com.example.shortenit.controller;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shortenit.dto.ShortenUrlRequest;
import com.example.shortenit.service.IUrlShorteningService;

import jakarta.validation.Valid;

@RestController
public class UrlController {
    private final IUrlShorteningService urlShorteningService;

    public UrlController(IUrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }

    @PostMapping("/api/v1/shorten")
    public ResponseEntity<String> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        System.out.println("Received long URL: " + request.getLongUrl());
        String shortUrl = urlShorteningService.shortenUrl(request.getLongUrl());
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToLongUrl(@PathVariable String shortUrl) {
        Optional<String> longUrl = urlShorteningService.getLongUrl(shortUrl);
        return longUrl
            .map(url -> ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", url)
                .build())
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
