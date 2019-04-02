package inc.pabacus.TaskMetrics.api.listener;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActivityListenerTest {

  private ActivityListener activityListener;

  private long i = 0;

  @Before
  public void init() {
    activityListener = new ActivityListenerService();
  }

  @Test
  public void testListenToEvent() throws InterruptedException {
    Runnable event = () -> i++;
    activityListener.setEvent(event);
    activityListener.setInterval(100); // override default 5 minute interval
    activityListener.listen();

    // the following events will send a mouse event to the listener
    Thread.sleep(50L);
    activityListener.invokeEvent();
    Thread.sleep(50L);
    activityListener.invokeEvent();
    Thread.sleep(50L);
    activityListener.invokeEvent();

    assertEquals(0, i); // Event should not be executed
  }

  @Test
  public void testUnListenToEvent() throws InterruptedException {
    Runnable event = () -> i++;
    activityListener.setEvent(event);
    activityListener.setInterval(100); // override default 5 minute interval
    activityListener.listen();
    activityListener.unListen();
    Thread.sleep(150L);
    assertEquals(0, i);
  }
}
