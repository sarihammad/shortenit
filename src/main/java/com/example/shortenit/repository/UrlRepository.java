package com.example.shortenit.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.example.shortenit.model.UrlMapping;

public interface UrlRepository extends CassandraRepository<UrlMapping, String> {
    
}
