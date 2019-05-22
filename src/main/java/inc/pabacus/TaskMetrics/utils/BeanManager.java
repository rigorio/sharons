package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.listener.ActivityListenerService;

public class BeanManager {
  private static ActivityListener activityListener = new ActivityListenerService();
  private static KickerService kickerService = new KickerService();
  private static ChatService chatService = new ChatService();

  public static ActivityListener activityListener() {
    return activityListener;
  }

  public static KickerService kickerService() {
    return kickerService;
  }

  public static ChatService chatService() {
    return chatService;
  }
}
