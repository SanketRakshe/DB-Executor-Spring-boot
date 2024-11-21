package com.example.demo.service;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.demo.repository.QueryRepository;

@Service
public class QueryService {

    @Autowired
    private QueryRepository queryRepository;

    private Properties queries;

    public QueryService() throws IOException {
        queries = new Properties();
        queries.load(new ClassPathResource("queries.properties").getInputStream());
    }

    public String executeQuery(String queryId) {
        String query = queries.getProperty(queryId);
        if (query == null) {
            return "Query ID " + queryId + " not found.";
        }
        return queryRepository.executeQuery(queryId, query);
    }
}