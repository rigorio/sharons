package inc.pabacus.TaskMetrics.desktop.tracker;

public class CountdownTimerConfiguration {
  private static boolean CountdownTimer;

  public static boolean isCountdownTimer() {
    return CountdownTimer;
  }

  public static void setCountdownTimer(boolean countdownTimer) {
    CountdownTimer = countdownTimer;
  }
}
