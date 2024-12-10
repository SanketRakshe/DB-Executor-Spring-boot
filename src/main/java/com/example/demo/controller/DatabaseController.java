package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DatabaseService;

import java.util.List;
import java.util.Map;

@RestController
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    // Endpoint to show all databases
    @GetMapping("/databases")
    public List<Map<String, Object>> getDatabases() {
        return databaseService.showDatabases();
    }

    // Endpoint to show all tables of a specific database
    @GetMapping("/tables/{databaseName}")
    public List<Map<String, Object>> getTables(@PathVariable String databaseName) {
        return databaseService.showTables(databaseName);
    }

    // Endpoint to describe a table
    @GetMapping("/describe/{databaseName}/{tableName}")
    public List<Map<String, Object>> describeTable(@PathVariable String databaseName, @PathVariable String tableName) {
        return databaseService.describeTable(databaseName, tableName);
    }
}

