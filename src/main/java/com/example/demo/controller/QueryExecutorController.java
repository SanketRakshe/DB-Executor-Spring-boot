package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.QueryService;

@RestController
@RequestMapping("/execute")
public class QueryExecutorController {

    @Autowired
    private QueryService queryService;

    @GetMapping("/{queryId}")
    public String executeQuery(@PathVariable("queryId") String queryId) {
        return queryService.executeQuery(queryId);
    }
}