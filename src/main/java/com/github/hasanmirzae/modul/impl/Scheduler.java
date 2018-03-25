package com.github.hasanmirzae.modul.impl;


import com.github.hasanmirzae.module.AbstractModule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduler extends AbstractModule<SchedulerStatus,ScheduledFuture<?>> {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Runnable task = null;
    private SchedulerStatus status = SchedulerStatus.INACTIVE;
    private long initialDelay;
    private long period;
    private TimeUnit unit;
    private ScheduledFuture<?> scheduledFuture;

    private Scheduler(){}

    public Scheduler(Runnable task, long initialDelay, long period, TimeUnit unit){
        this.task = new Task(task);
        this.period = period;
        this.initialDelay = initialDelay;
        this.unit = unit;
    }

    public ScheduledFuture<?> process(SchedulerStatus status){
        this.status = status;

        if(this.status == SchedulerStatus.ACTIVE){
            if(this.scheduledFuture == null || this.scheduledFuture.isCancelled())
                this.scheduledFuture = scheduler.scheduleAtFixedRate(this.task, initialDelay, period, unit);
        }
        else if(this.status == SchedulerStatus.INACTIVE){
            if (this.scheduledFuture != null)
                this.scheduledFuture.cancel(false);
        }
        return this.scheduledFuture;
    }

    class Task implements Runnable{

        private final Runnable task;
        Task(Runnable task){
            this.task = task;
        }

        @Override
        public void run() {
            try{
                this.task.run();
            }catch (Throwable e){
                handleException(e);
            }
        }
    }

}

