package com.example.lrfinalproject;

public class ExecutionTimer {


    private String tag;
    private long startTime;
    private long endTime;
    private long elapsedTimeMillis;

    public ExecutionTimer(String tag) {
        this.tag = tag;
    }

    public void startTime() {
        startTime = System.nanoTime();
    }

    public void endTime() {
        endTime = System.nanoTime();
        calcElapsedTime();
    }

    private void calcElapsedTime() {
        elapsedTimeMillis = ((endTime - startTime) / 1000000);
    }

    public long getElapsedTimeMillis() {
        return elapsedTimeMillis;
    }
}



