package com.example.lrfinalproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/query")
    public Query query(@RequestParam(value = "type", defaultValue = "search") String type,
                       @RequestParam(value = "start", defaultValue = "1900") String startYear,
                       @RequestParam(value = "end", defaultValue = "2100") String endYear,
                       @RequestParam(value = "term", defaultValue = "cancer") String searchTerm
    ) {
        return new Query(type, startYear, endYear, searchTerm);
    }
}