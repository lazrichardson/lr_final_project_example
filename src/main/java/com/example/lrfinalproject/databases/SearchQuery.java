package com.example.lrfinalproject.databases;

import java.util.ArrayList;

public class SearchQuery {
    //There should be a method to call and print the number of search terms, their timestamps, and frequency.

    String searchTerm;
    ArrayList<String> timeStamps;
    Integer frequency;

    public SearchQuery(String searchTerm, Integer frequency) {
        this.searchTerm = searchTerm;
        this.timeStamps = new ArrayList<>();
        this.frequency = frequency;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public void incrementFrequency() {
        frequency = frequency + 1;
    }

    public ArrayList<String> getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(ArrayList<String> timeStamps) {
        this.timeStamps = timeStamps;
    }

    public void addTimeStamp(String timeStamp) {
        timeStamps.add(timeStamp);
    }

    public void printTimeStamps() {
        if (timeStamps.isEmpty()) {
            System.out.print("No items to display");
        }
        for (String timeStamp : timeStamps) {
            System.out.println(timeStamp);
        }
        System.out.println();
    }

}
