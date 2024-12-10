package com.example.demo.service;

import com.example.demo.repository.QueryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class QueryServiceTest {

    @Autowired
    private QueryService queryService;

    @Autowired
    private QueryRepository queryRepository;

    @BeforeEach
    public void setup() {
        // Ensure the output folder is clean before each test
        File outputFolder = new File("query-results");
        if (outputFolder.exists()) {
            for (File file : outputFolder.listFiles()) {
                file.delete();
            }
        }
    }

    @Test
    public void testExecuteValidQuery() {
        String queryId = "Q1"; // CREATE TABLE employee
        String result = queryService.executeQuery(queryId);
        assertTrue(result.contains("Query executed successfully."));
    }

    @Test
    public void testExecuteInvalidQueryId() {
        String queryId = "Q999";
        String result = queryService.executeQuery(queryId);
        assertEquals("Query ID Q999 not found.", result);
    }

    @Test
    public void testExecuteSelectQuery() {
        // First, create the table and insert data
        queryService.executeQuery("Q1"); // CREATE TABLE
        queryService.executeQuery("Q2"); // INSERT INTO employee

        String result = queryService.executeQuery("Q3"); // SELECT * FROM employee
        assertTrue(result.contains("Query executed successfully. Results saved to output_Q3.csv"));

        // Verify the CSV file was created
        File csvFile = new File("query-results/output_Q3.csv");
        assertTrue(csvFile.exists(), "The output CSV file should exist.");
        assertTrue(csvFile.length() > 0, "The output CSV file should not be empty.");
    }
}