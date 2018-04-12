package com.github.hasanmirzae.modul.impl;

public class SampleTaskWithException implements Runnable{

    @Override
    public void run() {
        throw new RuntimeException();
    }
}
