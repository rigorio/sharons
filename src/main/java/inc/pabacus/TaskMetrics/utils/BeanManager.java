package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.listener.ActivityListenerService;

public class BeanManager {
  private static ActivityListener activityListener = new ActivityListenerService();
  private static KickerService kickerService = new KickerService();

  public static ActivityListener activityListener() {
    return activityListener;
  }

  public static KickerService kickerService() {
    return kickerService;
  }
}
