package com.github.hasanmirzae.modul.impl;

public class SampleTask implements Runnable {

    public SampleTask() {
    }

    @Override
    public void run() {
        SchedulerTest.count++;
    }
}
