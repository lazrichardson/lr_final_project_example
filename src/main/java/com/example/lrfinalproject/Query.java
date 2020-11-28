package com.example.lrfinalproject;

import com.example.lrfinalproject.databases.Article;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class Query {

    private final String type;
    private final String startYear;
    private final String endYear;
    private final String searchTerm;
    private final String results;

    public Query(String type, String startYear,
                 String endYear, String searchTerm, ArrayList<Article> results) throws IOException {
        this.type = type;
        this.startYear = startYear;
        this.endYear = endYear;
        this.searchTerm = searchTerm;
        this.results = writeListToJsonArray(results);
    }

    public void setResults() {

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

    public String getResults() {
        return results;
    }

    public String writeListToJsonArray(ArrayList<Article> results) throws IOException {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < results.size(); i++) {
            Article article = results.get(i);

            String newItem = "\"" + article.getArticleTitle() + "\",";

            if (i == (results.size() - 1)) {
                newItem = newItem.substring(0, (newItem.length() - 1));
            }
            builder.append(newItem);
        }
        return builder.toString();
    }
}
