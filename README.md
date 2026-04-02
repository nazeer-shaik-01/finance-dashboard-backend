# Finance Dashboard Backend

Spring Boot backend for finance dashboard with JWT auth, role-based access control, layered architecture, and MySQL persistence.

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- MySQL

## Package Structure
- `com.nazeer.finance.controller`
- `com.nazeer.finance.service`
- `com.nazeer.finance.repository`
- `com.nazeer.finance.entity`
- `com.nazeer.finance.config`
- `com.nazeer.finance.dto`

## APIs
### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Users
- `GET /api/users`
- `POST /api/users`
- `PUT /api/users/{id}`
- `PUT /api/users/{id}/roles`
- `DELETE /api/users/{id}`

### Financial Records
- `GET /api/records`
- `POST /api/records`
- `PUT /api/records/{id}`
- `DELETE /api/records/{id}`
- `GET /api/records/filter`

### Dashboard
- `GET /api/dashboard/summary`
- `GET /api/dashboard/category`
- `GET /api/dashboard/trends`
- `GET /api/dashboard/recent`

## Run
1. Configure environment variables:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `JWT_SECRET` (must be sufficiently long)
2. Start: `mvn spring-boot:run`
