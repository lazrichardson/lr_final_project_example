package com.example.lrfinalproject;

import java.util.ArrayList;

public class TimerHistory {

    private ArrayList<ExecutionTimer> timers;

    public TimerHistory(ArrayList<ExecutionTimer> timers) {
        this.timers = timers;
    }

    public ArrayList<ExecutionTimer> getTimers() {
        return timers;
    }
}
