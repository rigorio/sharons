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

  public void setTime(long second) {
    this.second = second;
  }

  /**
   * What does this even do?
   * What is thou purpose?
   */
  @Deprecated
  public void addSecond() {
    second++;
  }

  public void reset() {
    timer.cancel();
    second = 0;
  }

  public String formatSeconds(long seconds) {
    return String.format("%02d:%02d:%02d",
                         TimeUnit.SECONDS.toHours(seconds),
                         TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds)),
                         seconds - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds))
                        );
  }

  public long formatDuration(String duration) {
    String[] split = duration.split(":");
    String h = split[0];
    String m = split[1];
    String s = split[2];
    return Long.parseLong(h) * 3600 + Long.parseLong(m) * 60 + Long.parseLong(s);
  }

  /**
   * javafx.application.Platform#runLater(java.lang.Runnable) is specifically
   * used for invoking javafx related functions // i think
   *
   * @param process to be executed by TimerTask
   */
  public void setFxProcess(Runnable process) {
    timerTask = new TimerTask() {
      @Override
      public void run() {
        Platform.runLater(() -> {
          second--;
          process.run();
        });
      }
    };
  }

  /**
   * For normal processes
   *
   * @param process to be executed by TimerTask
   */
  public void setProcess(Runnable process) {
    timerTask = new TimerTask() {
      @Override
      public void run() {
        second++;
        process.run();
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
