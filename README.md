# Query Execution API with H2 Database

This Spring Boot application allows users to execute predefined SQL queries on an H2 database using REST APIs. The results of SELECT queries are saved in CSV files, while DML and DDL queries return the status and rows affected.

---

## Features
1. **H2 Database**:
   - Embedded H2 database for testing purposes.
   - Predefined tables and data included.

2. **Predefined Queries**:
   - Queries are defined in a `queries.properties` file.
   - Supports CRUD operations and joins.

3. **API for Execution**:
   - REST API to execute queries by query ID.

4. **CSV File Generation**:
   - Results of SELECT queries are stored in CSV files for further analysis.

---

## Prerequisites
1. **Java**: Java 17 installed.
2. **Maven**: Maven 3.x installed.
3. **Spring Tool Suite (STS 4)**: IDE for running and managing the project.

---

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone <repository_url>
   cd <repository_name>
   ```

2. **Build the Project**:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   - In STS 4, import the project as a Maven project.
   - Run the `DemoApplication.java` file as a Spring Boot application.

4. **API Endpoints**:
   - Base URL: `http://localhost:8080`
   - Execute a query:
     ```bash
     GET /execute/{queryId}
     ```
   - Replace `{queryId}` with the desired query ID from `queries.properties`.

5. **Predefined Queries**:
   - Queries are stored in the `src/main/resources/queries.properties` file.
   - Example:
     - `Q1`: Create `employee` table.
     - `Q3`: Fetch all records from `employee`.

---

## Example Usage
1. **Create Table**:
   ```bash
   curl -X GET "http://localhost:8080/execute/Q1"
   ```
2. **Insert Data**:
   ```bash
   curl -X GET "http://localhost:8080/execute/Q2"
   ```
3. **Select Data**:
   ```bash
   curl -X GET "http://localhost:8080/execute/Q3"
   ```
   - Results will be saved to `output_Q3.csv`.

4. **Update Data**:
   ```bash
   curl -X GET "http://localhost:8080/execute/Q4"
   ```

---

## Project Structure
```
src
├── main
│   ├── java
│   │   └── com.example.demo
│   │       ├── controller
│   │       │   └── QueryExecutorController.java
│   │       ├── repository
│   │       │   └── QueryRepository.java
│   │       ├── service
│   │       │   └── QueryService.java
│   │       └── DemoApplication.java
│   └── resources
│       ├── application.properties
│       └── queries.properties
└── test
    └── java
```

---

## Dependencies
- **Spring Boot Starter JDBC**
- **H2 Database**
- **OpenCSV**

Dependencies are managed in `pom.xml`. Ensure the following entries are present:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>com.opencsv</groupId>
        <artifactId>opencsv</artifactId>
        <version>5.7.1</version>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

---

## Notes
- The application uses an H2 in-memory database, which resets on application restart.
- Generated CSV files are saved in the project root directory.
- Queries must be valid SQL and follow the syntax for H2 Database.

---
