
# ToDoList API

## Introduction

ToDoList is a Spring Boot application providing a RESTful API for user authentication and task management. It allows users to register, log in, and manage their to-do tasks with CRUD operations. The application uses PostgreSQL as its database.

---

## Table of Contents

1. [Features](#features)
2. [Endpoints](#endpoints)
   - [User Management](#user-management)
   - [Task Management](#task-management)
3. [Installation](#installation)
4. [Configuration](#configuration)
5. [Troubleshooting](#troubleshooting)
6. [License](#license)

---

## Features

- User registration and authentication.
- Create, update, delete, and view tasks.
- Pagination support for task listing.
- Spring Security for authentication and authorization.

---

## Endpoints

### User Management

| Method | Endpoint           | Description                |
|--------|--------------------|----------------------------|
| POST   | `/api/v1/users/register` | Register a new user.       |
| POST   | `/api/v1/users/login`    | Log in an existing user.   |

#### User Payloads

**Register User (`/register`):**
```json
{
  "name": "string",
  "email": "string",
  "password": "string"
}
```

**Login User (`/login`):**
```json
{
  "email": "string",
  "password": "string"
}
```

**Token:**  
Upon successful login, a JSON Web Token (JWT) is returned. This token should be included in the `Authorization` header for all secured endpoints:  
```
Authorization: Bearer <your-token>
```

---

### Task Management

| Method   | Endpoint                    | Description                     |
|----------|-----------------------------|---------------------------------|
| POST     | `/api/vi/todos`             | Create a new task. Requires a valid token. |
| PUT      | `/api/vi/todos/{id}`        | Update an existing task. Requires a valid token. |
| DELETE   | `/api/vi/todos/{id}`        | Delete a specific task. Requires a valid token. |
| GET      | `/api/vi/todos/all`         | Get all tasks with pagination. Requires a valid token. |

#### Task Payloads

**Create/Update Task (`POST`/`PUT`):**
```json
{
  "title": "string",
  "description": "string"
}
```

**Pagination Parameters (`/all`):**
- `page` (optional, default: 0): The page number.
- `limit` (optional, default: 10): Number of tasks per page.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/todolist.git
   cd todolist
   ```

2. Build the application:
   ```bash
   ./mvnw clean install
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## Configuration

### Application YAML

The application configuration is stored in `application.yaml`. Below is the database configuration:

```yaml
spring:
  application:
    name: ToDoList
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/todolist
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
```

> **Note:** Update the `url` to match your database settings.

---

## Troubleshooting

1. **Lombok Issues**:  
   If Lombok annotations are not working, enable annotation processing in your IDE:
   - For IntelliJ IDEA:
     - Go to **File** > **Settings** > **Build, Execution, Deployment** > **Compiler** > **Annotation Processors**.
     - Check **Enable annotation processing**.

2. **Database Connection**:  
   Ensure PostgreSQL is running and accessible. Verify the database URL in `application.yaml`.

3. **Dependencies**:  
   Run the following command to ensure all dependencies are installed:
   ```bash
   ./mvnw dependency:resolve
   ```

---

