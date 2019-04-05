package inc.pabacus.TaskMetrics.utils;

import javafx.application.Platform;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Service
public class TimerService {

  private static final Long DELAY = 1000L;
  private static final Long PERIOD = 1000L; // 1 second

  private TimerTask timerTask;
  private Timer timer;
  private long second = 0;

  public TimerService() {
    timer = new Timer("Timer");
  }

  public long getTime() {
    return second;
  }

  public void addSecond() {
    second++;
  }

  public void reset() {
    second = 0;
  }

  public String formatSeconds(long duration) {
    return String.format("%02d:%02d:%02d",
                         TimeUnit.SECONDS.toHours(duration),
                         TimeUnit.SECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(duration)),
                         duration - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(duration))
    );
  }

  public void setProcess(Runnable process) {
    timerTask = new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(() -> {
          second++;
          process.run();
        });
      }
    };
  }

  public void changeTask(TimerTask task) {
    timerTask = task;
  }

  /**
   * TODO make it so that if this is invoked without an instantiated process, throw a RunTimeException
   */
  public void start() {
    timer.scheduleAtFixedRate(timerTask, DELAY, PERIOD);
  }

  public void start(TimerTask timerTask) {
    timer.scheduleAtFixedRate(timerTask, DELAY, PERIOD);
  }

  public void start(Long delay, Long interval) {
    timer.scheduleAtFixedRate(timerTask, delay, interval);
  }

  public void start(TimerTask timerTask, Long delay, Long interval) {
    timer.scheduleAtFixedRate(timerTask, delay, interval);
  }

  public void pause() {
    timerTask.cancel();
  }
}
