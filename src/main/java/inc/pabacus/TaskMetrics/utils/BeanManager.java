package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.api.hardware.HardwareServiceAPI;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.listener.ActivityListenerService;
import inc.pabacus.TaskMetrics.api.screenshot.ScreenshotServiceImpl;
import inc.pabacus.TaskMetrics.api.software.SoftwareServiceAPI;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.user.UserHandler;

public class BeanManager {
  private static ActivityListener activityListener = new ActivityListenerService();
  private static KickerService kickerService = new KickerService();
  private static ChatService chatService = new ChatService();
  private static StandupService standupService = new StandupService();
  private static HardwareServiceAPI hardwareService = new HardwareServiceAPI();
  private static SoftwareServiceAPI softwareServiceAPI = new SoftwareServiceAPI();
  private static ScreenshotServiceImpl screenshotService = new ScreenshotServiceImpl();
  private static UserHandler userHandler = new UserHandler();
  private static DailyLogService dailyLogService = new DailyLogHandler();
  private static XpmTaskWebHandler xpmTaskHandler = new XpmTaskWebHandler();

  public static ActivityListener activityListener() {
    return activityListener;
  }

  public static KickerService kickerService() {
    return kickerService;
  }

  public static ChatService chatService() {
    return chatService;
  }

  public static StandupService standupService() {
    return standupService;
  }

  public static HardwareServiceAPI hardwareServiceAPI() {
    return hardwareService;
  }

  public static SoftwareServiceAPI softwareServiceAPI() {
    return softwareServiceAPI;
  }

  public static ScreenshotServiceImpl screenshotService() {
    return screenshotService;
  }

  public static UserHandler userHandler() {
    return userHandler;
  }

  public static DailyLogService dailyLogService() {
    return dailyLogService;
  }

  public static XpmTaskWebHandler xpmTaskHandler() {
    return xpmTaskHandler;
  }
}
