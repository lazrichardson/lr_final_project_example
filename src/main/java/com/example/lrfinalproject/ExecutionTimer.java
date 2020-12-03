package com.example.lrfinalproject;

public class ExecutionTimer {


    private String tag;
    private String searchString;
    private int numResults;
    private long startTime;
    private long endTime;
    private long elapsedTimeMillis;

    public ExecutionTimer(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void startTime() {
        startTime = System.nanoTime();
    }

    public void endTime() {
        endTime = System.nanoTime();
        calcElapsedTime();
    }

    public void setSearchString(String term, String start, String end){
        this.searchString = "Search for papers containing the word " + term
                + " from " + start + " to " + end
                + " using " + tag;
    }

    public void setNumResults(int numResults){
        this.numResults = numResults;
    }

    private void calcElapsedTime() {
        elapsedTimeMillis = ((endTime - startTime) / 1000000);
    }

    public long getElapsedTimeMillis() {
        return elapsedTimeMillis;
    }

    public String getSearchString() {
        return searchString;
    }
}



