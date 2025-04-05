# ShortenIt – Scalable URL Shortener with Distributed Hashing, Redis Caching, and Kafka

**ShortenIt** is a high-performance, production-grade URL shortening service built to handle high traffic with speed, resilience, and fault tolerance.  

Designed using a distributed systems approach, it combines **Spring Boot**, **Cassandra**, **Kafka**, and **Redis** to ensure scalable storage, fast lookups, and asynchronous processing.

---

## Key Features

- Generates short, unique identifiers for long URLs
- Implements **consistent hashing** to evenly distribute data across nodes
- Uses a **Bloom filter** to prevent duplicate entries
- Caches frequent lookups with **Redis** for ultra-fast performance
- Leverages **Kafka** for event-driven, high-throughput processing
- Stores persistent mappings in **Cassandra** for scalability and durability
- Designed to support **10k+ QPS** with low latency

---

## Architecture Overview

| Component | Role |
|----------|------|
| **Shortener Service** | Accepts URLs, generates unique short links |
| **Redis Cache** | Stores hot keys for fast retrieval |
| **Cassandra DB** | Durable key-value storage using consistent hashing |
| **Bloom Filter** | Prevents duplicates without hitting the DB |
| **Kafka Queue** | Handles async processing for high volume input |
| **Docker Compose** | Manages multi-service dev environment |

---

## Tech Stack

| Layer | Tools |
|-------|-------|
| **Backend** | Java 17, Spring Boot |
| **Storage** | Cassandra |
| **Caching** | Redis |
| **Messaging** | Apache Kafka |
| **Infra** | Docker, Docker Compose |
| **Monitoring (Planned)** | Prometheus + Grafana |

---

## ⚙️ Setup & Run Locally

### 1. Clone the Repository
```sh
git clone https://github.com/sarihammad/shortenit.git
cd shortenit
```

### 2. Start Dependencies using Docker
```sh
docker-compose up -d
```

### 3. Configure Environment Variables
Modify `application.yml` or `application.properties`:
```yaml
spring:
  data:
    cassandra:
      contact-points: cassandra
      port: 9042
      keyspace-name: url_shortener
      username: cassandra
      password: cassandra
    redis:
      host: redis
      port: 6379
kafka:
  bootstrap-servers: kafka:9092
```

### 4. Start the Application
```sh
./mvnw spring-boot:run
```

### 5. Test API Endpoints
#### Shorten a URL
```sh
curl -X POST http://localhost:8080/api/shorten -H "Content-Type: application/json" -d '{"url": "https://example.com"}'
```

#### Retrieve Original URL
```sh
curl -X GET http://localhost:8080/api/v1/{shortened_id}
```

## Scaling Strategy
- Cassandra is used with **consistent hashing** to distribute data evenly.
- Redis caches popular URLs to minimize database hits.
- Kafka handles large traffic loads asynchronously.
- Horizontal scaling with Docker.

