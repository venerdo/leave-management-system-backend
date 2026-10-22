# Leave Management System Backend

**Student:** Ushindi Bihame Victoire  
**Student ID:** 27269  
**Course:** INSY 8322 Web Technology and Internet  
**Project:** Leave Management System Mid-Term Examination  

## About the Project
This is a complete Spring Boot backend for a Leave Management System. It features a layered MVC architecture (Model, Repository, Service, Controller) and connects to a PostgreSQL database. It includes full CRUD operations, derived queries, custom JPQL queries, business rules, and pagination.

## Steps to Run the API

### Prerequisites
- Java 21 (or higher, tested with Java 25)
- Maven
- PostgreSQL

### Database Setup
1. Open PostgreSQL (via pgAdmin or terminal) and create the database and user:
```sql
CREATE USER leave_user WITH PASSWORD 'You_Can_Add_Your_Password';
CREATE DATABASE leave_management_db OWNER leave_user;
GRANT ALL PRIVILEGES ON DATABASE leave_management_db TO leave_user;
```

2. The application.properties file is already configured to connect to localhost:5432 with the credentials above.

### Running the Application

```shell
./mvnw spring-boot:run
```

3. The API will start on `http://localhost:8080`