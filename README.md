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

| Component           | Role                                           |
|---------------------|------------------------------------------------|
| Shortener Service   | Accepts URLs, generates short links            |
| Redis Cache         | Stores hot keys for fast retrieval             |
| Cassandra DB        | Durable key-value storage w/ consistent hashing |
| Bloom Filter        | Prevents duplicates before DB writes           |
| Kafka Queue         | Handles async processing and traffic spikes    |
| Docker Compose      | Spins up the full stack for local dev          |

---

## Tech Stack

| Layer           | Tools                          |
|----------------|----------------------------------|
| Backend         | Java 17, Spring Boot             |
| Storage         | Cassandra                        |
| Caching         | Redis                            |
| Messaging       | Apache Kafka                     |
| Containerization| Docker, Docker Compose           |
| Monitoring      | Prometheus + Grafana (planned)   |

---

## ⚙Setup & Run Locally

### 1. Clone the Repo

```bash
git clone https://github.com/sarihammad/shortenit.git  
cd shortenit
```

### 2. Start Dependencies

```bash
docker compose up -d
```

### 3. Configure Spring Application

Edit `application.yml`:

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

### 4. Run the App

```bash
./mvnw spring-boot:run
```

---

## API Testing

### Shorten a URL

```bash
curl -X POST http://localhost:8080/api/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://example.com"}'
```

### Retrieve Original URL

```bash
curl -X GET http://localhost:8080/api/v1/{shortened_id}
```

---

## Scaling Strategy

- Cassandra uses **consistent hashing** for horizontal scalability  
- Redis caches frequently accessed URLs for high-speed lookup  
- Kafka handles message queues for async processing at scale  
- Fully containerized for easy deployment and orchestration

---

## Why This Project?

**ShortenIt** demonstrates:
- High-traffic system design  
- Use of consistent hashing in production  
- Redis + Kafka integration for latency and throughput  
- Real-world backend engineering principles  
- Microservice and distributed systems best practices

---

## Status

MVP complete and running locally  
Planned: Kubernetes or Swarm deployment  
Planned: Observability with Prometheus + Grafana  

---

## GitHub

[https://github.com/sarihammad/shortenit](https://github.com/sarihammad/shortenit)
