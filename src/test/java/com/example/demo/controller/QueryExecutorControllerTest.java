package com.example.demo.controller;

import com.example.demo.service.QueryService;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QueryExecutorController.class)
public class QueryExecutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryService queryService;

    @Test
    public void testExecuteQuery() throws Exception {
        String queryId = "Q3";
        String expectedResponse = "Query executed successfully. Results saved to output_Q3.csv";

        Mockito.when(queryService.executeQuery(queryId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/execute/{queryId}", queryId))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testExecuteQuery_NotFound() throws Exception {
        String queryId = "Q999";
        String expectedResponse = "Query ID Q999 not found.";

        Mockito.when(queryService.executeQuery(queryId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/execute/{queryId}", queryId))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }
}