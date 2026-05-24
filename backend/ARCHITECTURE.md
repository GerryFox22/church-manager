# Church Manager — Backend Architecture

Spring Boot 4 application with **feature-based packaging**, JWT authentication, and PostgreSQL persistence.

## Package layout

```
com.gerardo.churchmanager.backend/
├── BackendApplication.java          # Entry point
├── auth/                            # Authentication feature
│   ├── controller/                  # REST: /api/auth/*
│   ├── dto/                         # LoginRequest, RegisterRequest, AuthResponse
│   └── service/                     # AuthService, JwtService
├── user/                            # User domain
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── enums/                       # Role (ADMIN, USER)
│   ├── repository/
│   └── service/
├── news/                            # News CRUD feature
│   ├── controller/                  # REST: /api/news
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── events/                          # Events CRUD feature
│   ├── controller/                  # REST: /api/events
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── security/                        # Cross-cutting security
│   ├── SecurityConfig.java          # Filter chain, role rules
│   └── JwtFilter.java               # Bearer token validation
└── common/                          # Shared infrastructure
    ├── config/                      # WebConfig (static uploads)
    └── exception/                   # GlobalExceptionHandler, custom errors
```

## Layer responsibilities

| Layer        | Responsibility                                      |
|-------------|------------------------------------------------------|
| `controller`| HTTP mapping, validation (`@Valid`), status codes    |
| `dto`       | API contracts (request/response), no JPA annotations |
| `entity`    | JPA persistence model                                |
| `repository`| Spring Data JPA access                               |
| `service`   | Business logic, transactions                         |
| `security`  | Authentication & authorization                       |
| `common`    | Cross-feature config and error handling              |

## API surface

| Method | Path              | Auth        | Description        |
|--------|-------------------|-------------|--------------------|
| POST   | `/api/auth/login` | Public      | Returns JWT        |
| POST   | `/api/auth/register` | Public   | Register user      |
| GET    | `/api/news`       | Public      | List news          |
| POST   | `/api/news`       | ADMIN       | Create (multipart) |
| PUT    | `/api/news/{id}`  | ADMIN       | Update (multipart) |
| DELETE | `/api/news/{id}`  | ADMIN       | Delete             |
| GET    | `/api/events`     | Public      | List events        |
| POST   | `/api/events`     | ADMIN       | Create (multipart) |
| PUT    | `/api/events/{id}`| ADMIN       | Update (multipart) |
| DELETE | `/api/events/{id}`| ADMIN       | Delete             |

JWT payload includes `role` (`ADMIN` | `USER`) for frontend authorization.

## Enterprise extensions (optional)

- `application-{profile}.yml` for dev/staging/prod
- Dedicated `mapper` package (MapStruct) between entity and DTO
- `dashboard` analytics endpoint if KPIs are needed server-side
- Integration tests under `src/test/java/.../integration/`
