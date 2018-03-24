package com.github.hasanmirzae.modul.impl;

public class SchedulerConfig{

    private int intervalSeconds;

    public SchedulerConfig(int intervalSeconds){
        this.intervalSeconds = intervalSeconds;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }
}
