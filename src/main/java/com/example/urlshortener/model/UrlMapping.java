package com.example.urlshortener.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

@Table("urls")
@Data
public class UrlMapping {
    @PrimaryKey
    private String shortUrl;
    private String longUrl;
}
