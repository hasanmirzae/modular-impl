package com.github.hasanmirzae.modul.impl;


import com.github.hasanmirzae.module.AbstractConfigurableModule;
import com.github.hasanmirzae.module.AbstractModule;
import com.github.hasanmirzae.module.Configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Scheduler extends AbstractConfigurableModule<SchedulerStatus,ScheduledFuture<?>> {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Runnable task = null;
    private SchedulerStatus status = SchedulerStatus.INACTIVE;
    private long initialDelay;
    private long period;
    private TimeUnit unit;
    private ScheduledFuture<?> scheduledFuture;




    @Override
    protected Function<SchedulerStatus, ScheduledFuture<?>> getLogic() {
        return stat ->{
            status = stat;
            if(status == SchedulerStatus.ACTIVE){
                if(scheduledFuture == null || scheduledFuture.isCancelled())
                    scheduledFuture = scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
            }
            else if(status == SchedulerStatus.INACTIVE){
                if (scheduledFuture != null)
                    scheduledFuture.cancel(false);
            }
            return scheduledFuture;
        };
    }



    public Scheduler(Configuration config){
        super(config);
        configure(config);
    }

    @Override
    protected Collection<String> requiredConfigNames() {
        return Arrays.asList("unit","taskName","period","initialDelay");
    }


    private void configure(Configuration configuration) {
        try {
            task = new Task(getTaskByName(configuration.get("taskName")));
            period = Long.valueOf(configuration.get("period"));
            initialDelay = Long.valueOf(configuration.get("initialDelay"));
            unit = TimeUnit.valueOf(configuration.get("unit"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private Runnable getTaskByName(String name)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            return (Runnable) Class.forName(name).newInstance();
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

