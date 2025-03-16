package com.example.urlshortener.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.example.urlshortener.model.UrlMapping;

public interface UrlRepository extends CassandraRepository<UrlMapping, String> {
    
}
