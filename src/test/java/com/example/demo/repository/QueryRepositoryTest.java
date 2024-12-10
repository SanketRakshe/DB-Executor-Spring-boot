package com.example.demo.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryRepositoryTest {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String OUTPUT_FOLDER = "query-results";

    @BeforeEach
    public void setup() {
        // Clean up the output folder
        File outputFolder = new File(OUTPUT_FOLDER);
        if (outputFolder.exists()) {
            for (File file : outputFolder.listFiles()) {
                file.delete();
            }
        }
    }

    @Test
    @Order(1)
    public void testExecuteCreateTableQuery() {
        String queryId = "Q1";
        String query = "CREATE TABLE IF NOT EXISTS employee (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, salary DECIMAL(15,2) NOT NULL);";

        String result = queryRepository.executeQuery(queryId, query);

        assertTrue(result.contains("Query executed successfully."), "The query should execute successfully.");
    }

    @Test
    @Order(2)
    public void testExecuteInsertQuery() {
        // First, create the table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS employee (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, salary DECIMAL(15,2) NOT NULL);");

        String queryId = "Q2";
        String query = "INSERT INTO employee (name, salary) VALUES ('Pranav', 20000.00);";

        String result = queryRepository.executeQuery(queryId, query);

        assertTrue(result.contains("Rows affected: 1"), "One row should be affected.");

        // Verify data in the database
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee WHERE name = 'Pranav'", Integer.class);
        assertEquals(1, count, "One record should exist in the employee table.");
    }

    @Test
    @Order(3)
    public void testExecuteSelectQuery() {
        // Ensure there is data to select
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS employee (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, salary DECIMAL(15,2) NOT NULL);");
        jdbcTemplate.execute("INSERT INTO employee (name, salary) VALUES ('Pranav', 20000.00);");

        String queryId = "Q3";
        String query = "SELECT * FROM employee;";

        String result = queryRepository.executeQuery(queryId, query);

        assertTrue(result.contains("Results saved to output_Q3.csv"), "The results should be saved to a CSV file.");

        // Verify the CSV file
        File csvFile = new File(OUTPUT_FOLDER + File.separator + "output_" + queryId + ".csv");
        assertTrue(csvFile.exists(), "The CSV file should exist.");

        // Optionally, read the CSV file and verify contents
        try {
            String content = new String(Files.readAllBytes(Paths.get(csvFile.getPath())));
            assertTrue(content.contains("Pranav"), "The CSV file should contain the data.");
        } catch (Exception e) {
            fail("Failed to read the CSV file.");
        }
    }

    @Test
    @Order(4)
    public void testExecuteUpdateQuery() {
        // Ensure there is data to update
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS employee (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, salary DECIMAL(15,2) NOT NULL);");
        jdbcTemplate.execute("INSERT INTO employee (name, salary) VALUES ('Pranav', 20000.00);");

        String queryId = "Q4";
        String query = "UPDATE employee SET salary = 55000.00 WHERE name = 'Pranav';";

        String result = queryRepository.executeQuery(queryId, query);

        assertTrue(result.contains("Rows affected: 1"), "One row should be affected.");

        // Verify the update
        Double salary = jdbcTemplate.queryForObject("SELECT salary FROM employee WHERE name = 'Pranav'", Double.class);
        assertEquals(55000.00, salary, "Salary should be updated to 55000.00.");
    }

    @Test
    @Order(5)
    public void testExecuteDeleteQuery() {
        // Ensure there is data to delete
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS employee (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, salary DECIMAL(15,2) NOT NULL);");
        jdbcTemplate.execute("INSERT INTO employee (name, salary) VALUES ('Pranav', 20000.00);");

        String queryId = "Q5";
        String query = "DELETE FROM employee WHERE name = 'Pranav';";

        String result = queryRepository.executeQuery(queryId, query);

        assertTrue(result.contains("Rows affected: 1"), "One row should be affected.");

        // Verify the deletion
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee WHERE name = 'Pranav'", Integer.class);
        assertEquals(0, count, "No records should exist in the employee table.");
    }

    @Test
    @Order(6)
    public void testExecuteInvalidQuery() {
        String queryId = "Invalid";
        String query = "INVALID SQL QUERY";

        String result = queryRepository.executeQuery(queryId, query);

        assertTrue(result.contains("Error executing query"), "An error message should be returned.");
    }

    @Test
    @Order(7)
    public void testExecuteSelectWithCondition() {
        // Prepare data
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS employee (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL, salary DECIMAL(15,2) NOT NULL);");
        jdbcTemplate.execute("INSERT INTO employee (name, salary) VALUES ('Alice', 60000.00), ('Bob', 40000.00);");

        String queryId = "Q6";
        String query = "SELECT name FROM employee WHERE salary > 50000.00;";

        String result = queryRepository.executeQuery(queryId, query);

        assertTrue(result.contains("Results saved to output_Q6.csv"), "The results should be saved to a CSV file.");

        // Verify the CSV file
        File csvFile = new File(OUTPUT_FOLDER + File.separator + "output_" + queryId + ".csv");
        assertTrue(csvFile.exists(), "The CSV file should exist.");

        // Optionally, read the CSV file and verify contents
        try {
            String content = new String(Files.readAllBytes(Paths.get(csvFile.getPath())));
            assertTrue(content.contains("Alice"), "The CSV file should contain 'Alice'.");
            assertFalse(content.contains("Bob"), "The CSV file should not contain 'Bob'.");
        } catch (Exception e) {
            fail("Failed to read the CSV file.");
        }
    }
}