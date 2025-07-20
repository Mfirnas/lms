# LMS Project

This is the Learning Management System (LMS) Spring Boot project.

## Description
A brief description of your project, its features, and usage.
This project is a **Learning Management System (LMS)** developed using **Java 17** and **Spring Boot 3**. It provides a robust set of REST APIs to manage books and members, and supports key transactions like borrowing and returning books.

### Features

- **Add a Book**  
  Allows adding new books to the system with metadata like title, author, and ISBN.


- **Register a Member**  
  Enables creation and management of library members.


- **Borrow a Book**  
  A member can borrow an available book. Includes validations for already borrowed or reserved books.


- **Return a Book**  
  A borrowed book can be returned, updating its availability status.


- **CRUD Operations**  
  Complete Create, Read, Update, and Delete operations for both books and members.


- **Spring Security with JWT**  
  Endpoints are secured using **JWT-based authentication** via Spring Security (Stateless mode using SWT). Token fetching and validation are implemented.


- **Role-Based Access (RBAC)**  
  The **Delete Book** API is restricted to users with the `ADMIN` role for demonstration/testing purposes.


- **Implemented pagination, sorting, and searching functionality for books, allowing users to search by book title or author, sort results, and navigate through pages of data efficiently.**


- **JUnit and Integration tests added**


- **Sample data added for testing**


- **Interactive API Documentation**  
  APIs are accessible via Swagger UI:  
  [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

---


## How to run
## How to Run the Project

Follow these steps to set up and run the LMS Spring Boot application locally:

### 1. Clone the Repository

```bash
git clone https://github.com/Mfirnas/lms.git
```

Open the project in your IDE (IntelliJ is recommended).

---

### 2. Update Dependencies

Ensure all dependencies are downloaded and the project builds successfully:

```bash
./mvnw clean install
```

---

### 3. Set Up PostgreSQL Database

- Create a new PostgreSQL database (recommended name: `lms`).
- Set the **schema name** appropriately (`public` or your custom).
- Update the database credentials in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/lms
    username: your_postgres_username
    password: your_postgres_password
```

---

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

If successful, open:  
[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

---

### 5. Authentication Setup (In-Memory Users)

Two in-memory users are available for testing:

| Username | Password  | Role   |
|----------|-----------|--------|
| admin    | admin123  | ADMIN  |
| user     | user123   | USER   |

---

### 6. Using Swagger for API Testing

1. Open Swagger and find the `/login` endpoint.
2. Authenticate using one of the credentials above.
3. Copy the **token** from the response (excluding `"` or extra text).
4. Click **"Authorize"** (top-right corner).
5. Paste the token to authorize all secured endpoints.

> ✅ Now you can access all APIs.  
> 🔐 Only `DELETE /book/{id}` requires **ADMIN** role.

---

You're all set to use the LMS API locally!

## Author
Mohamed Firnas
