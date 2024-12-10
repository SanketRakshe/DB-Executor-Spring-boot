package com.example.demo.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.opencsv.CSVWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

@Repository
public class QueryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final String OUTPUT_FOLDER = "query-results";

    public String executeQuery(String queryId, String query) {
        // Determine the type of query: SELECT or DML/DDL
        String queryType = query.trim().split("\\s+")[0].toUpperCase();
        try {
        	
        	createOutputFolder();
        	
            if (queryType.equals("SELECT")) {
                // Execute SELECT query
                jdbcTemplate.query(query, new ResultSetExtractor<Void>() {
                    @Override
                    public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
                        try {
                        	String filePath = OUTPUT_FOLDER + File.separator + "output_" + queryId + ".csv";
                            writeResultSetToCSV(rs, filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
                return "Query executed successfully. Results saved to output_" + queryId + ".csv";
            } else {
                // Execute DML or DDL query
                int rowsAffected = jdbcTemplate.update(query);
                return "Query executed successfully. Rows affected: " + rowsAffected;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing query: " + e.getMessage();
        }
    }

    private void writeResultSetToCSV(ResultSet rs, String fileName) throws SQLException, IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            // Write header
            int columnCount = rs.getMetaData().getColumnCount();
            String[] header = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                header[i] = rs.getMetaData().getColumnName(i + 1);
            }
            writer.writeNext(header);

            // Write data
            while (rs.next()) {
                String[] data = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    data[i] = rs.getString(i + 1);
                }
                writer.writeNext(data);
            }
        }
    }
    
    private void createOutputFolder() {
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs(); // Creates the folder and necessary parent directories
        }
    }
    
}