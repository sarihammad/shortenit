package com.example.shortenit.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlMapping {
    @PrimaryKey
    @Size(max = 10)
    private String shortUrl;

    @NotBlank
    private String longUrl;
}
