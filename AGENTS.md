# AGENTS.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

BMI Manager is a Spring Boot 3.1.5 web application for tracking Body Mass Index (BMI) and weight over time. The UI and documentation are in Polish. It uses an in-memory H2 database that resets on restart, with test data auto-loaded via `DataInitializer`.

## Build and Run Commands

```bash
# Build the project
mvn clean install

# Run the application (accessible at http://localhost:8080)
mvn spring-boot:run

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=TestClassName

# Run a single test method
mvn test -Dtest=TestClassName#methodName

# Build JAR for deployment
mvn clean package
```

## Test Accounts

- **Admin**: `admin` / `admin123` (has ROLE_ADMIN)
- **Demo user**: `demo` / `demo123`

## Architecture

### Facade Pattern
The codebase uses the Facade pattern via `BMIFacadeService` which aggregates:
- `UserService` - user CRUD and profile management
- `WeightService` - weight records and BMI calculations

Controllers should primarily interact with `BMIFacadeService` rather than individual services directly.

### Layer Structure
```
controller/  → HTTP endpoints (Thymeleaf + REST)
service/     → Business logic (BMIFacadeService as facade)
repository/  → Spring Data JPA interfaces
entity/      → JPA entities (User, WeightRecord)
config/      → Security, WebSocket, Authentication
init/        → DataInitializer for test data
```

### Security
- Spring Security with form login and Basic Auth
- BCrypt password encoding
- Roles: `ROLE_USER`, `ROLE_ADMIN` (admin determined by username == "admin")
- `/admin/**` endpoints require ROLE_ADMIN
- CSRF is disabled

## Code Conventions

- **Classes**: `PascalCase`
- **Methods/variables**: `camelCase`
- **Constants**: `UPPER_SNAKE_CASE`
- **Database columns**: `snake_case`
- Use constructor injection for dependencies
- Templates use Thymeleaf with **Pure.css** (not Materialize CSS)

## H2 Console

Available at http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:bmidb`
- Username: `admin`
- Password: `admin`
