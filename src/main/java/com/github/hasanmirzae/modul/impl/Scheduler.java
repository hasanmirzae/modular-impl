package com.github.hasanmirzae.modul.impl;


import com.github.hasanmirzae.modul.AbstractModule;

import java.util.concurrent.TimeUnit;

public class Scheduler extends AbstractModule<SchedulerStatus,Void> {

    private final SchedulerConfig config;
    private final Runnable task;
    private final Thread taskRunner;
    private SchedulerStatus status = SchedulerStatus.INACTIVE;

    public Scheduler(SchedulerConfig config, Runnable task){
        this.config = config;
        this.task = task;
        this.taskRunner = createRunner();
    }

    private Thread createRunner() {
        String error = validateRequirements();
        if(error == null)
        return createThread();
        else
        {
            handleException(new IllegalArgumentException(error));
            return null;
        }
    }

    private Thread createThread() {
        return new Thread(()->{
                while (status == SchedulerStatus.ACTIVE){
                    try {
                        task.run();
                    TimeUnit.SECONDS.sleep(config.getIntervalSeconds());
                    } catch (Throwable e) {
                        handleException(e);
                    }
                }
        });
    }

    private String validateRequirements() {
        if (this.config == null)
            return "Configuration is null";
        if (this.task == null)
            return "Task is not defined";
        return null;
    }

    @Override
    public void invoke(SchedulerStatus input) {
        process(input);
    }

    public Void process(SchedulerStatus status){
        this.status = status;
        if (this.taskRunner == null)
            return null;
        if(!taskRunner.isAlive() && this.status == SchedulerStatus.ACTIVE)
            taskRunner.start();
        else if(taskRunner.isAlive() && this.status == SchedulerStatus.INACTIVE)
            taskRunner.interrupt();
        return  null;
    }

}

