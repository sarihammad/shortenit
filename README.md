# ShortenIt: URL Shortener with Distributed Hashing

## Overview
This project is a high-performance URL shortener built using **Spring Boot**, **Cassandra**, **Redis**, and **Kafka**. It provides fast lookups and high availability, leveraging **consistent hashing** for efficient load distribution.

## Features
- Shortens long URLs with unique identifiers.
- Uses **consistent hashing** to distribute data across multiple nodes.
- Implements **Bloom Filters** to prevent duplicate URL generation.
- Stores mappings in **Cassandra** for scalability and fault tolerance.
- Uses **Redis** for caching frequently accessed URLs.
- Processes high traffic loads efficiently using **Kafka**.

## Tech Stack
- **Backend:** Spring Boot
- **Database:** Cassandra (for persistent storage)
- **Cache:** Redis (for fast lookups)
- **Message Queue:** Kafka (for handling high traffic)

## Architecture
1. **URL Shortening Service**: Generates short URLs and stores them in Cassandra.
2. **Consistent Hashing**: Distributes data across multiple Cassandra nodes.
3. **Bloom Filter**: Prevents duplicate URL entries before inserting into the database.
4. **Redis Cache**: Speeds up lookups by caching frequently accessed URLs.
5. **Kafka**: Handles event-driven processing for high throughput.

## Prerequisites
- Docker & Docker Compose
- Java 17+
- Apache Kafka
- Apache Cassandra
- Redis

## Setup Instructions

### 1. Clone the Repository
```sh
git clone https://github.com/sarihammad/url-shortener.git
cd url-shortener
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
curl -X GET http://localhost:8080/api/{shortened_id}
```

## Scaling Strategy
- Cassandra is used with **consistent hashing** to distribute data evenly.
- Redis caches popular URLs to minimize database hits.
- Kafka handles large traffic loads asynchronously.
- Horizontal scaling with Docker and Kubernetes is supported.

