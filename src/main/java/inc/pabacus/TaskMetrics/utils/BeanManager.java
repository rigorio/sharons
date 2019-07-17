package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.api.generateToken.TokenService;
import inc.pabacus.TaskMetrics.api.hardware.HardwareServiceAPI;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.listener.ActivityListenerService;
import inc.pabacus.TaskMetrics.api.screenshot.ScreenshotServiceImpl;
import inc.pabacus.TaskMetrics.api.software.SoftwareServiceAPI;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.status.StatusUpdateHandler;
import inc.pabacus.TaskMetrics.api.timesheet.status.ValidationHandler;
import inc.pabacus.TaskMetrics.api.timesheet.time.TimeLogHandler;
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
  private static DailyLogHandler dailyLogService = new DailyLogHandler();
  private static XpmTaskWebHandler xpmTaskHandler = new XpmTaskWebHandler();
  private static ActivityHandler activityHandler = new ActivityHandler();
  private static TokenService tokenService = new TokenService();
  private static LeaveService leaveService = new LeaveService();
  private static StatusUpdateHandler statusUpdateHandler = new StatusUpdateHandler();
  private static ValidationHandler validationHandler = new ValidationHandler();
  private static TimeLogHandler timeLogHandler = new TimeLogHandler();

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

  public static DailyLogHandler dailyLogService() {
    return dailyLogService;
  }

  public static XpmTaskWebHandler xpmTaskHandler() {
    return xpmTaskHandler;
  }

  public static ActivityHandler activityHandler() {
    return activityHandler;
  }

  public static TokenService tokenService() {
    return tokenService;
  }

  public static LeaveService leaveService() {
    return leaveService;
  }

  public static StatusUpdateHandler statusUpdateHandler() {
    return statusUpdateHandler;
  }

  public static TimeLogHandler timeLogHandler() {
    return timeLogHandler;
  }

  // I am too lazy to provide an explanation as to why
  // this is found below all else
  private static ServiceManager serviceManager = new ServiceManager();

  public static void activate() {
    serviceManager.activate();
  }

  public static void deactivate() {
    serviceManager.deactivate();
  }

  public static ValidationHandler validationHandler() {
    return validationHandler;
  }
}
