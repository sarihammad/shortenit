# ShortenIt

**ShortenIt** is a high-performance, production-grade URL shortening service built to handle high traffic with speed, resilience, and fault tolerance.  

Designed using a distributed systems approach, it combines **Spring Boot**, **Cassandra**, **Kafka**, and **Redis** to ensure scalable storage, fast lookups, and asynchronous processing.

## Key Features

- Generates short, unique identifiers for long URLs  
- Implements **consistent hashing** to evenly distribute data across nodes  
- Uses a **Bloom filter** to prevent duplicate entries  
- Caches frequent lookups with **Redis** for ultra-fast performance  
- Leverages **Kafka** for event-driven, high-throughput processing  
- Stores persistent mappings in **Cassandra** for scalability and durability  
- Designed to support **10k+ QPS** with low latency  

## Architecture Overview

| Component           | Role                                           |
|---------------------|------------------------------------------------|
| Shortener Service   | Accepts URLs, generates short links            |
| Redis Cache         | Stores hot keys for fast retrieval             |
| Cassandra DB        | Durable key-value storage w/ consistent hashing |
| Bloom Filter        | Prevents duplicates before DB writes           |
| Kafka Queue         | Handles async processing and traffic spikes    |
| Docker Compose      | Spins up the full stack for local dev          |

## Tech Stack

| Layer           | Tools                          |
|----------------|----------------------------------|
| Backend         | Java 17, Spring Boot             |
| Storage         | Cassandra                        |
| Caching         | Redis                            |
| Messaging       | Apache Kafka                     |
| Containerization| Docker, Docker Compose           |
| Monitoring      | Prometheus + Grafana (planned)   |


