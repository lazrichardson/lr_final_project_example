package com.example.lrfinalproject;

public class Query {

    private final String type;
    private final String startYear;
    private final String endYear;
    private final String searchTerm;

    public Query(String type, String startYear,
                 String endYear, String searchTerm) {
        this.type = type;
        this.startYear = startYear;
        this.endYear = endYear;
        this.searchTerm = searchTerm;
    }

    public String getType() {
        return type;
    }

    public String getStartYear() {
        return startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public String getSearchTerm() {
        return searchTerm;
    }
}
