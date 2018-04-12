package com.github.hasanmirzae.modul.impl;
import com.github.hasanmirzae.module.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchedulerTest {

    public static int count = 0;
    private boolean exceptionHandled = false;
    private Configuration config;

    @Before
    public void setup(){
        config = new Configuration()
                .add("period","100")
                .add("unit","MILLISECONDS")
                .add("initialDelay","0")
                .add("taskName",SampleTask.class.getName());

    }

    @Test
    public void shouldNotRunIfInvokedWithInactiveStatus(){
        count = 0;
        new Scheduler(config)
                .invoke(SchedulerStatus.INACTIVE);
        sleepSeconds(1);
        assertEquals(0,count);
    }




    @Test
    public void testActivationDeActivationSchedule(){
        count = 0;
        Scheduler scheduler = new Scheduler(config);
        scheduler.onException(System.out::println);
        // start schedule
        scheduler.invoke(SchedulerStatus.ACTIVE);
        sleepSeconds(1);
        assertTrue(count > 0);
        count = 0;
        // stop schedule
        scheduler.invoke(SchedulerStatus.INACTIVE);
        sleepSeconds(1);
        // check if task is not called after cancelling scheduler
        assertEquals(0,count);
        // start scheduler back
        count = 0;
        scheduler.invoke(SchedulerStatus.ACTIVE);
        sleepSeconds(1);
        assertTrue(count > 0);
    }



    @Test
    public void exceptionHandlerShouldBeCalled(){
        exceptionHandled = false;
        Configuration config = new Configuration()
                .add("period","100")
                .add("unit","MILLISECONDS")
                .add("initialDelay","0")
                .add("taskName",SampleTaskWithException.class.getName());
        new Scheduler(config)
                .onException(e-> exceptionHandled = true)
                .invoke(SchedulerStatus.ACTIVE);
        sleepSeconds(1);
        assertTrue(exceptionHandled);
    }

    private void sleepSeconds(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }
}
