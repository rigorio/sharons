package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.api.hardware.HardwareServiceAPI;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.listener.ActivityListenerService;
import inc.pabacus.TaskMetrics.api.software.SoftwareServiceAPI;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;

public class BeanManager {
  private static ActivityListener activityListener = new ActivityListenerService();
  private static KickerService kickerService = new KickerService();
  private static ChatService chatService = new ChatService();
  private static StandupService standupService = new StandupService();
  private static HardwareServiceAPI hardwareService = new HardwareServiceAPI();
  private static SoftwareServiceAPI softwareServiceAPI = new SoftwareServiceAPI();

  public static ActivityListener activityListener() {
    return activityListener;
  }

  public static KickerService kickerService() {
    return kickerService;
  }

  public static ChatService chatService() {
    return chatService;
  }

  public static HardwareServiceAPI hardwareServiceAPI() {
    return hardwareService;
  }

  public static SoftwareServiceAPI softwareServiceAPI() {
    return softwareServiceAPI;
  }
}
