# Auth Service — Spring Boot OAuth2 + JWT

A standalone Spring Boot 3 service that provides user registration, login, and JWT-based authentication/authorization using Spring Security's OAuth2 Resource Server.

## Architecture Overview

```
┌──────────────┐      POST /auth/login       ┌──────────────────┐
│   Client     │ ──────────────────────────►  │  AuthController   │
│              │ ◄──────────────────────────  │                  │
│              │   { accessToken, refresh }   │  ► AuthService    │
│              │                              │  ► JwtService     │
│              │  GET /api/secure             │                  │
│              │  Authorization: Bearer xxx   ├──────────────────┤
│              │ ──────────────────────────►  │  SecurityConfig   │
│              │                              │  (OAuth2 RS +JWT) │
└──────────────┘                              └────────┬─────────┘
                                                       │
                                                       ▼
                                              ┌──────────────────┐
                                              │   PostgreSQL      │
                                              │   (users table)   │
                                              └──────────────────┘
```

## Components

| Layer | Class | Purpose |
|-------|-------|---------|
| **Entity** | `UserEntity`, `Role` | JPA entity mapped to `users` table; `Role` enum for RBAC |
| **Repository** | `UserRepository` | Spring Data JPA interface for user persistence |
| **Service** | `AuthService` | Orchestrates register/login/refresh workflows |
| **Service** | `JwtService` | Signs and issues JWT access + refresh tokens using RSA |
| **Service** | `CustomUserDetailsService` | Loads users from DB for Spring Security authentication |
| **Controller** | `AuthController` | Public `/auth/**` endpoints |
| **Controller** | `SecureController` | Protected `/api/**` endpoints demonstrating RBAC |
| **Config** | `SecurityConfig` | Spring Security filter chain, JWT encoder/decoder, BCrypt |
| **Config** | `DataInitializer` | Seeds a default admin user on first startup |

## Endpoints

| Method | Path | Auth Required | Description |
|--------|------|--------------|-------------|
| POST | `/auth/register` | No | Register a new user |
| POST | `/auth/login` | No | Authenticate and receive tokens |
| POST | `/auth/refresh` | No | Exchange a refresh token for new tokens |
| GET | `/api/secure` | Yes (any role) | Example protected endpoint |
| GET | `/api/admin` | Yes (ROLE_ADMIN) | Admin-only endpoint |

## Prerequisites

- **Java 21+**
- **Maven 3.9+**
- **PostgreSQL 15+** running on `localhost:5432`

## Database Setup

```bash
# Create the database
psql -U postgres -c "CREATE DATABASE auth_db;"
```

Hibernate will auto-create the `users` table on startup (`ddl-auto: update`).

## Running Locally

```bash
cd auth-service

# Generate RSA keys (already included, but to regenerate):
# mkdir -p src/main/resources/certs
# openssl genrsa -out keypair.pem 2048
# openssl rsa -in keypair.pem -pubout -out src/main/resources/certs/public.pem
# openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt \
#   -in keypair.pem -out src/main/resources/certs/private.pem
# rm keypair.pem

# Build and run
./mvnw spring-boot:run

# Or with custom DB credentials:
DB_USERNAME=myuser DB_PASSWORD=mypass ./mvnw spring-boot:run
```

The service starts on **port 8081**.

## Usage Examples

### Register a user

```bash
curl -s -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "securepass123"
  }' | jq .
```

### Login

```bash
curl -s -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "securepass123"
  }' | jq .
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

### Access a protected endpoint

```bash
TOKEN="<accessToken from login response>"
curl -s http://localhost:8081/api/secure \
  -H "Authorization: Bearer $TOKEN" | jq .
```

### Refresh tokens

```bash
curl -s -X POST http://localhost:8081/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<refreshToken from login response>"
  }' | jq .
```

### Access admin endpoint (requires ROLE_ADMIN)

```bash
# Login as the seeded admin user
curl -s -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}' | jq .

# Use the admin's access token
curl -s http://localhost:8081/api/admin \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq .
```

## Security Design Notes

- **Stateless sessions** — No server-side session state; every request is authenticated via JWT.
- **RSA-256 signing** — Tokens are signed with an RSA private key and verified with the public key. This allows other services to validate tokens using only the public key.
- **Separate token types** — Access tokens (15 min) carry role claims; refresh tokens (24 h) carry only `token_type: refresh` and cannot be used as access tokens.
- **BCrypt password hashing** — Passwords are hashed with BCrypt (strength 10) before storage.
- **Role-based access control** — The `role` claim in the JWT is mapped to a Spring Security `GrantedAuthority`, enabling `@PreAuthorize` and URL-pattern-based authorization.
