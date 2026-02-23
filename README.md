# Payment Service

Payment Processing service with Kafka integration.

## Port
8083

## Features
- Payment processing REST API
- Kafka producer for successful payments
- Publishes to "settlements" topic
- OAuth2 JWT token validation
- Virtual Threads enabled
- Caffeine Caching

## Tech Stack
- Spring Boot 3.5
- Java 21
- PostgreSQL
- Liquibase
- Apache Kafka
- Spring Security OAuth2

## Database
```sql
CREATE DATABASE payment_db;
```

## Kafka Setup
```bash
kafka-topics.sh --create --topic settlements --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

## Running
```bash
mvn spring-boot:run
```

## Testing
```bash
mvn test
```

## API Endpoints
- `POST /api/payments/process` - Process payment (requires USER/ADMIN role)

## Integration
Publishes successful payment events to Kafka "settlements" topic for Settlement Service consumption.
