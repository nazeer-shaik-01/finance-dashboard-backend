# Finance Dashboard Backend

A production-ready Spring Boot REST API for a personal finance dashboard. Provides JWT-based authentication, financial record management, and analytics endpoints.

## Tech Stack

- **Java 17** + **Spring Boot 3.2.4**
- **Spring Security** (JWT, stateless)
- **Spring Data JPA** + **H2** (in-memory, dev) / **MySQL** (prod)
- **Lombok**, **Bean Validation**, **SpringDoc OpenAPI**

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Run (H2 in-memory, no setup needed)
```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080`.

### Default Admin Credentials
| Field    | Value               |
|----------|---------------------|
| Email    | admin@finance.com   |
| Password | admin123            |

## API Endpoints

### Authentication — `/api/auth`
| Method | Path               | Description            |
|--------|--------------------|------------------------|
| POST   | `/register`        | Register new user      |
| POST   | `/login`           | Login, get JWT tokens  |
| POST   | `/refresh`         | Refresh access token   |

### Financial Records — `/api/records`
| Method | Path          | Role           | Description               |
|--------|---------------|----------------|---------------------------|
| GET    | `/`           | Any auth       | Paginated record list     |
| POST   | `/`           | ANALYST, ADMIN | Create record             |
| GET    | `/{id}`       | Any auth       | Get record by ID          |
| PUT    | `/{id}`       | ANALYST, ADMIN | Update record             |
| DELETE | `/{id}`       | ANALYST, ADMIN | Soft-delete record        |
| GET    | `/filter`     | Any auth       | Filter by date/type/cat   |
| GET    | `/recent`     | Any auth       | Recent transactions       |

### Dashboard — `/api/dashboard`
| Method | Path                   | Description             |
|--------|------------------------|-------------------------|
| GET    | `/summary`             | Income/expense totals   |
| GET    | `/category-breakdown`  | Breakdown by category   |
| GET    | `/monthly-trends`      | Monthly income/expense  |
| GET    | `/period-analysis`     | Analysis for a period   |
| GET    | `/recent-transactions` | Recent transactions     |

### Users — `/api/users` (Admin only)
| Method | Path            | Description          |
|--------|-----------------|----------------------|
| GET    | `/`             | All users            |
| POST   | `/`             | Create user          |
| GET    | `/{id}`         | Get user             |
| PUT    | `/{id}`         | Update user          |
| DELETE | `/{id}`         | Delete user          |
| PUT    | `/{id}/role`    | Assign role          |
| PUT    | `/{id}/status`  | Update status        |

### Categories — `/api/categories`
| Method | Path    | Description       |
|--------|---------|-------------------|
| GET    | `/`     | All categories    |
| GET    | `/{id}` | Category by ID    |

## Roles

| Role    | Permissions                                   |
|---------|-----------------------------------------------|
| ADMIN   | Full access — users, records, dashboard       |
| ANALYST | Create/update/delete records + dashboard      |
| VIEWER  | Read-only — records and dashboard             |

## Useful URLs

| URL                                      | Description        |
|------------------------------------------|--------------------|
| `http://localhost:8080/swagger-ui.html`  | Swagger UI         |
| `http://localhost:8080/api-docs`         | OpenAPI JSON       |
| `http://localhost:8080/h2-console`       | H2 Database Console|

## Authentication

All protected endpoints require a Bearer token in the `Authorization` header:

```
Authorization: Bearer <access_token>
```

Obtain the token via `POST /api/auth/login`.

## MySQL Configuration

To switch to MySQL, update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/financedb?useSSL=false&serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: your_user
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
```

## Build

```bash
mvn clean package
java -jar target/dashboard-0.0.1-SNAPSHOT.jar
```
