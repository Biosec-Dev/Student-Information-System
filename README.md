# OBS Backend

This is the backend for the Online Booking System, built with Spring Boot. It provides a RESTful API for the Angular frontend.

## Features

- JWT-based authentication
- Student and Teacher role-based access control
- Course enrollment with approval workflow
- Student-Teacher adviser relationship management

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository
2. Navigate to the project directory: `cd obs-backend`
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

The server will start on port 8080.

## API Endpoints

### Authentication

- `POST /api/auth/login` - Authenticate a user and return JWT token

### Student Endpoints

- `GET /api/students/profile` - Get the profile of the logged-in student
- `GET /api/students/courses/active` - Get active courses for the logged-in student
- `GET /api/students/courses/pending` - Get pending courses for the logged-in student
- `POST /api/students/courses/request` - Request enrollment in a course
- `POST /api/students/adviser` - Change student's adviser

### Teacher Endpoints

- `GET /api/teachers/profile` - Get the profile of the logged-in teacher
- `GET /api/teachers/courses` - Get courses taught by the logged-in teacher
- `GET /api/teachers/advisees` - Get students advised by the logged-in teacher
- `POST /api/teachers/courses/approve` - Approve a student's course enrollment request

## Database

The application uses an H2 in-memory database. The console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, Username: `sa`, Password: empty).

## Testing

Test data is preloaded from `src/main/resources/data.sql`.

Available users:
- Student: `alice@example.com` / `password`
- Student: `bob@example.com` / `password`
- Teacher: `john.doe@example.com` / `password`
- Teacher: `jane.smith@example.com` / `password` 