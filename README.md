# DG-ECM: Enterprise Case Management System

A Spring Boot application for enterprise case management with Spring Modulith and multi-tenant support, packaged as a WAR file with Liquibase database migrations.

## Features

- **Multi-Tenant Architecture**: Header-based tenant identification with automatic data isolation
- **Spring Modulith**: Modular architecture with clear boundaries and dependencies
- **DTO Pattern**: Clean separation between JPA entities and API layer using MapStruct
- **Case Management**: Complete case lifecycle management with notes and attachments
- **User Management**: User registration, authentication, and role-based access control
- **JWT Authentication**: Stateless authentication with JWT tokens
- **RESTful API**: Comprehensive REST endpoints for all operations
- **Database Support**: H2 (development) and PostgreSQL (production) support
- **Liquibase Migrations**: Version-controlled database schema and data changes
- **WAR Deployment**: Deployable as a standard Java web application
- **Audit Trail**: Automatic audit fields (created/updated timestamps, user tracking)

## Technology Stack

- **Spring Boot 3.2.0**
- **Spring Modulith 1.1.3**
- **Spring Security** with JWT
- **Spring Data JPA**
- **Liquibase** for database migrations
- **MapStruct 1.5.5** for DTO mapping
- **Lombok** for reducing boilerplate
- **H2 Database** (development)
- **PostgreSQL** (production)
- **Apache Tomcat** (WAR deployment)
- **Maven**

## Project Structure

```
src/main/java/com/enterprise/ecm/
├── DgEcmApplication.java          # Main application class (WAR-enabled)
├── auth/                          # Authentication module
│   ├── AuthController.java
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   └── dto/
│       └── LoginResponse.java
├── cases/                         # Case management module
│   ├── Case.java
│   ├── CaseController.java
│   ├── CaseRepository.java
│   ├── CaseService.java
│   ├── CaseStatus.java
│   ├── CasePriority.java
│   ├── CaseNote.java
│   ├── CaseAttachment.java
│   ├── dto/                       # Case DTOs
│   │   ├── CaseDto.java
│   │   ├── CreateCaseRequest.java
│   │   ├── UpdateCaseRequest.java
│   │   ├── CaseNoteDto.java
│   │   ├── CreateCaseNoteRequest.java
│   │   └── CaseAttachmentDto.java
│   └── mapper/                    # MapStruct mappers
│       ├── CaseMapper.java
│       ├── CaseNoteMapper.java
│       └── CaseAttachmentMapper.java
├── users/                         # User management module
│   ├── User.java
│   ├── UserController.java
│   ├── UserRepository.java
│   ├── UserService.java
│   ├── dto/                       # User DTOs
│   │   ├── UserDto.java
│   │   ├── CreateUserRequest.java
│   │   └── UpdateUserRequest.java
│   └── mapper/                    # MapStruct mappers
│       └── UserMapper.java
├── security/                      # Security configuration
│   ├── SecurityConfig.java
│   ├── JwtTokenProvider.java
│   └── JwtAuthenticationFilter.java
├── shared/                        # Shared components
│   ├── entity/
│   │   └── BaseEntity.java
│   └── tenant/
│       ├── TenantContext.java
│       ├── TenantInfo.java
│       └── TenantInterceptor.java
└── config/
    └── JpaConfig.java

src/main/resources/
├── db/changelog/                  # Liquibase database migrations
│   ├── db.changelog-master.xml    # Main changelog file
│   └── changes/                   # Individual changelog files
│       ├── 001-initial-schema.xml # Database schema creation
│       └── 002-sample-data.xml    # Initial data insertion
├── application.yml                # Application configuration
└── webapp/WEB-INF/
    └── web.xml                    # Web application configuration
```

## Database Migrations with Liquibase

The application uses Liquibase for database schema and data management:

### Migration Files
- **001-initial-schema.xml**: Creates all database tables, indexes, and constraints
- **002-sample-data.xml**: Inserts initial sample data for development

### Benefits of Liquibase
- **Version Control**: All database changes are tracked and versioned
- **Environment Consistency**: Same schema across all environments
- **Rollback Support**: Ability to rollback database changes
- **Team Collaboration**: Multiple developers can work on database changes safely
- **Production Safety**: Controlled database deployments

### Running Migrations
Liquibase automatically runs migrations on application startup. You can also run them manually:

```bash
# Check migration status
mvn liquibase:status

# Run migrations
mvn liquibase:update

# Rollback to a specific version
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

## DTO Architecture

The application uses a clean DTO pattern with MapStruct for automatic mapping:

### DTO Types
- **Request DTOs**: For API input (e.g., `CreateCaseRequest`, `UpdateUserRequest`)
- **Response DTOs**: For API output (e.g., `CaseDto`, `UserDto`)
- **Internal DTOs**: For internal communication between modules

### MapStruct Mappers
- **CaseMapper**: Maps between Case entities and DTOs
- **UserMapper**: Maps between User entities and DTOs
- **CaseNoteMapper**: Maps between CaseNote entities and DTOs
- **CaseAttachmentMapper**: Maps between CaseAttachment entities and DTOs

### Benefits
- **Separation of Concerns**: JPA entities are separate from API contracts
- **API Evolution**: Can evolve API without affecting database schema
- **Security**: Control what data is exposed through APIs
- **Validation**: Different validation rules for API vs database
- **Performance**: Can optimize DTOs for specific use cases

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL (for production)
- Apache Tomcat 10+ (for WAR deployment)

### Running the Application

#### Development Mode (JAR)
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd dg-ecm
   ```

2. **Build the application**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

#### Production Mode (WAR)
1. **Build the WAR file**
   ```bash
   mvn clean package
   ```

2. **Deploy to Tomcat**
   - Copy `target/dg-ecm-1.0.0.war` to `$TOMCAT_HOME/webapps/`
   - Rename to `ROOT.war` for root context deployment
   - Start Tomcat server

3. **Using Docker**
   ```bash
   docker build -t dg-ecm .
   docker run -p 8080:8080 dg-ecm
   ```

The application will start on `http://localhost:8080`

### Default Credentials

Test Users and Passwords
Based on your sample data, here are the available test users:

Admin User
Username: admin
Email: admin@example.com
Password: password123
Role: ADMIN + USER
Department: IT
Position: System Administrator
Support User

Username: user1
Email: user1@example.com
Password: password123
Role: USER
Department: Support
Position: Support Agent
Sales User

Username: user2
Email: user2@example.com
Password: password123
Role: USER
Department: Sales
Position: Sales Representative

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/me` - Get current user
- `POST /api/auth/logout` - User logout

### Case Management
- `GET /api/cases` - Get all cases (paginated)
- `POST /api/cases` - Create new case
- `GET /api/cases/{id}` - Get case by ID
- `PUT /api/cases/{id}` - Update case
- `DELETE /api/cases/{id}` - Delete case
- `GET /api/cases/status/{status}` - Get cases by status
- `GET /api/cases/priority/{priority}` - Get cases by priority
- `GET /api/cases/assignee/{assignedTo}` - Get cases by assignee
- `GET /api/cases/overdue` - Get overdue cases
- `GET /api/cases/search?keyword={keyword}` - Search cases
- `PATCH /api/cases/{id}/status` - Update case status
- `PATCH /api/cases/{id}/assign` - Assign case

### User Management (Admin only)
- `GET /api/users` - Get all users (paginated)
- `POST /api/users` - Create new user
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `PATCH /api/users/{id}/activate` - Activate user
- `PATCH /api/users/{id}/deactivate` - Deactivate user

## API Request/Response Examples

### Create Case
```bash
POST /api/cases
Content-Type: application/json
X-TenantID: company-a
Authorization: Bearer your-jwt-token

{
  "title": "System Login Issue",
  "description": "User cannot login to the system",
  "priority": "HIGH",
  "category": "Technical",
  "assignedTo": "support-team"
}
```

### Create User
```bash
POST /api/users
Content-Type: application/json
X-TenantID: company-a
Authorization: Bearer your-jwt-token

{
  "username": "john.doe",
  "email": "john.doe@company.com",
  "password": "securepassword123",
  "firstName": "John",
  "lastName": "Doe",
  "department": "IT",
  "position": "System Administrator",
  "roles": ["USER", "ADMIN"]
}
```

### Login Response
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin"
}
```

## Multi-Tenant Usage

The application supports multi-tenancy through HTTP headers:

1. **Set tenant header** in your requests:
   ```
   X-TenantID: your-tenant-id
   ```

2. **Default tenant** is used if no header is provided:
   ```
   X-TenantID: default
   ```

3. **Tenant isolation** is automatically enforced at the database level

### Example API calls with tenant header:

```bash
# Create a case for tenant "company-a"
curl -X POST http://localhost:8080/api/cases \
  -H "X-TenantID: company-a" \
  -H "Authorization: Bearer your-jwt-token" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New Case",
    "description": "Case description",
    "priority": "HIGH"
  }'

# Get cases for tenant "company-b"
curl -X GET http://localhost:8080/api/cases \
  -H "X-TenantID: company-b" \
  -H "Authorization: Bearer your-jwt-token"
```

## Database Configuration

### Development (H2)
The application uses H2 in-memory database by default. Access the H2 console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:ecmdb`
- Username: `sa`
- Password: (empty)

### Production (PostgreSQL)
Update `application.yml` for PostgreSQL:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecmdb
    username: your-username
    password: your-password
    driver-class-name: org.postgresql.Driver
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

## Security

### JWT Configuration
Update JWT settings in `application.yml`:

```yaml
jwt:
  secret: your-secure-secret-key-here
  expiration: 86400000  # 24 hours in milliseconds
```

### Roles and Permissions
- **ADMIN**: Full access to all endpoints
- **USER**: Access to case management and own user profile

## Development

### Building with Spring Modulith
```bash
# Generate module documentation
mvn spring-modulith:documentation

# Generate module structure diagram
mvn spring-modulith:documentation -Dspring-modulith.documentation.diagram=true
```

### MapStruct Development
```bash
# Generate MapStruct mappers
mvn compile

# Run tests to verify mappers
mvn test
```

### Testing
```bash
# Run all tests
mvn test

# Run specific module tests
mvn test -Dtest=CaseServiceTest

# Run mapper tests
mvn test -Dtest=CaseMapperTest
```

## Deployment

### Docker
```bash
# Build Docker image
docker build -t dg-ecm .

# Run container
docker run -p 8080:8080 dg-ecm
```

### Production Considerations
1. Use PostgreSQL instead of H2
2. Configure proper JWT secret
3. Set up proper logging
4. Configure CORS if needed
5. Set up monitoring and health checks
6. Use environment variables for sensitive configuration
7. Ensure MapStruct mappers are properly generated in production builds

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new DTOs and mappers
5. Submit a pull request

## License

This project is licensed under the MIT License. 