import com.github.hasanmirzae.modul.impl.Scheduler;
import com.github.hasanmirzae.modul.impl.SchedulerStatus;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchedulerTest {

    private int count = 0;
    private boolean exceptionHandled = false;

    @Test
    public void taskShouldBeCalledBySchedule(){
        count = 0;
        new Scheduler(this::sampleTask,0,100, TimeUnit.MILLISECONDS)
                .invoke(SchedulerStatus.ACTIVE);
        sleepSeconds(1);
        assertTrue(count >= 10);
    }
    private void sampleTask(){
        count++;
    }

    @Test
    public void shouldNotRunIfInvokedWithInactiveStatus(){
        count = 0;
        new Scheduler(this::sampleTask,0,100, TimeUnit.MILLISECONDS)
                .invoke(SchedulerStatus.INACTIVE);
        sleepSeconds(1);
        assertEquals(0,count);
    }

    @Test
    public void testActivationDeActivationSchedule(){
        count = 0;
        Scheduler scheduler =  new Scheduler(this::sampleTask,0,100, TimeUnit.MILLISECONDS);
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
        new Scheduler(()->{throw new RuntimeException();},0,100, TimeUnit.MILLISECONDS)
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
