package inc.pabacus.TaskMetrics.api.listener;

/**
 * @author Rigo Sarmiento 03/27/19
 * @see ActivityListenerService
 */
public interface ActivityListener {

  /**
   * @param milliseconds interval to check user activity
   */
  void setInterval(int milliseconds);

  Runnable getEvent();

  /**
   * @param event to be executed every time the user is suspected to be idle
   */
  void setEvent(Runnable event);

  /**
   * start listening to user activity
   */
  void listen();

  void unListen();

  /**
   * retrieve last recorded time in millis of user activity
   *
   * @return
   */
  long getLastTime();

  /**
   * @return the interval
   */
  int getInterval();

}
