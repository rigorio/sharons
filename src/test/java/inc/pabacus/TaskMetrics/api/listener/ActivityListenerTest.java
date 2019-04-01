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

  /**
   * Test is temporarily ignored as it is not
   * 100% fool-proof. The results are dependent on
   * whether or not user activity is detected
   *
   * @throws InterruptedException
   */
  @Test
  @Ignore
  public void testListenToEvent() throws InterruptedException {
    Runnable event = () -> i++;
    activityListener.setEvent(event);
    activityListener.setInterval(100); // override default 5 minute interval
    activityListener.listen();
    Thread.sleep(150L);
    assertEquals(1, i);
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
