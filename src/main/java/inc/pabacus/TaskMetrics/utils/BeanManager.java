package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.listener.ActivityListenerService;

public class BeanManager {
  private static ActivityListener activityListener = new ActivityListenerService();

  public static ActivityListener activityListener() {
    return activityListener;
  }
}
