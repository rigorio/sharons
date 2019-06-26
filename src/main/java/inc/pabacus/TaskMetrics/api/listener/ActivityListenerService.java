package inc.pabacus.TaskMetrics.api.listener;

import org.apache.log4j.Logger;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ActivityListenerService implements ActivityListener {

  private static final int INITIAL_DELAY = 0;
  private static final Logger logger = Logger.getLogger(ActivityListenerService.class);
  private ScheduledExecutorService executorService;

  private long previousTime = 0;
  private long lastTime = System.currentTimeMillis();
  private int interval = 10000; // in milliseconds = 5 minutes
  private boolean hasRunBefore = false;

  private NativeKeyAndMouseListener listener;
  private ScheduledFuture<?> scheduledFuture;
  private Runnable event;
  private Runnable runnable = () -> {
    previousTime = lastTime;
    lastTime = listener.getTime();
    long timeGap = lastTime - previousTime;
    System.out.println(timeGap);
    if (timeGap == 0)
      event.run();
  };

  public ActivityListenerService() {
    listener = new NativeKeyAndMouseListener();
  }

  @Override
  public int getInterval() {
    return interval;
  }

  @Override
  public void setInterval(int interval) {
    if (interval < 100)
      throw new IllegalArgumentException("Interval should be in milliseconds. The minimum is 100 milliseconds.");
    this.interval = interval;
  }

  @Override
  public Runnable getEvent() {
    return event;
  }

  @Override
  public void setEvent(Runnable event) {
    this.event = event;
  }

  @Override
  public void listen() {
    try {
      if (event == null)
        throw new NoEventFoundException("Executable `event` not yet defined");
      executorService = Executors.newSingleThreadScheduledExecutor();
      listener.listen();
      int delay = hasRunBefore ? interval : 0;
      System.out.println(delay);
      scheduledFuture = executorService.scheduleWithFixedDelay(runnable, delay, interval, TimeUnit.MILLISECONDS);
    } catch (NativeHookException | NoEventFoundException e) {
      logger.warn(e.getMessage());
    }
  }

  @Override
  public void unListen() {
    try {
      hasRunBefore = true;
      if (scheduledFuture == null)
        throw new RuntimeException("Listener not activated");
      scheduledFuture.cancel(true);
      listener.unListen();
    } catch (NativeHookException e) {
      logger.warn(e.getMessage());
    }
  }

  @Override
  public long getLastTime() {
    return listener.getTime();
  }

  @Override
  public void invokeEvent() {
    listener.nativeMouseMoved(new NativeMouseEvent(0, 0L, 0, 0, 0, 0));
  }
}
