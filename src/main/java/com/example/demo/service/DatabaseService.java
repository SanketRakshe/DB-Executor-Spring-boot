package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to show all databases
    public List<Map<String, Object>> showDatabases() {
        String query = "SHOW DATABASES;";
        return jdbcTemplate.queryForList(query);
    }

    // Show all tables in the specified database
    public List<Map<String, Object>> showTables(String databaseName) {
        // Dynamically connect to the database
        String query = "SHOW TABLES";
        return jdbcTemplate.queryForList(query);
    }

    // Describe a specific table
    public List<Map<String, Object>> describeTable(String databaseName, String tableName) {
        String query = "DESCRIBE " + tableName; // DESCRIBE the table
        return jdbcTemplate.queryForList(query);
    }
}
