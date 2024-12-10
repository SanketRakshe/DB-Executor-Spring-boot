package com.example.demo.controller;

import com.example.demo.service.DatabaseService;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DatabaseController.class)
public class DatabaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseService databaseService;

    @Test
    public void testGetDatabases() throws Exception {
        // Prepare mock data
        Map<String, Object> db1 = new HashMap<>();
        db1.put("Database", "testdb1");
        Map<String, Object> db2 = new HashMap<>();
        db2.put("Database", "testdb2");
        List<Map<String, Object>> databases = Arrays.asList(db1, db2);

        Mockito.when(databaseService.showDatabases()).thenReturn(databases);

        mockMvc.perform(get("/databases"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].Database").value("testdb1"))
            .andExpect(jsonPath("$[1].Database").value("testdb2"));
    }

    @Test
    public void testGetTables() throws Exception {
        String databaseName = "testdb";
        Map<String, Object> table1 = new HashMap<>();
        table1.put("Tables_in_" + databaseName, "table1");
        Map<String, Object> table2 = new HashMap<>();
        table2.put("Tables_in_" + databaseName, "table2");
        List<Map<String, Object>> tables = Arrays.asList(table1, table2);

        Mockito.when(databaseService.showTables(databaseName)).thenReturn(tables);

        mockMvc.perform(get("/tables/{databaseName}", databaseName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].Tables_in_" + databaseName).value("table1"))
            .andExpect(jsonPath("$[1].Tables_in_" + databaseName).value("table2"));
    }

    @Test
    public void testDescribeTable() throws Exception {
        String databaseName = "testdb";
        String tableName = "testtable";
        Map<String, Object> column1 = new HashMap<>();
        column1.put("Field", "id");
        column1.put("Type", "int");
        Map<String, Object> column2 = new HashMap<>();
        column2.put("Field", "name");
        column2.put("Type", "varchar(255)");
        List<Map<String, Object>> columns = Arrays.asList(column1, column2);

        Mockito.when(databaseService.describeTable(databaseName, tableName)).thenReturn(columns);

        mockMvc.perform(get("/describe/{databaseName}/{tableName}", databaseName, tableName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].Field").value("id"))
            .andExpect(jsonPath("$[0].Type").value("int"))
            .andExpect(jsonPath("$[1].Field").value("name"))
            .andExpect(jsonPath("$[1].Type").value("varchar(255)"));
    }
}