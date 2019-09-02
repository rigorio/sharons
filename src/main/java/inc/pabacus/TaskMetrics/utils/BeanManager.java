package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheService;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.api.chat.ChatService;
import inc.pabacus.TaskMetrics.api.generateToken.AuthenticatorService;
import inc.pabacus.TaskMetrics.api.hardware.HardwareServiceAPI;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.listener.ActivityListenerService;
import inc.pabacus.TaskMetrics.api.screenshot.ScreenshotServiceImpl;
import inc.pabacus.TaskMetrics.api.software.SoftwareServiceAPI;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.api.tasks.TaskConnector;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.status.StatusUpdateHandler;
import inc.pabacus.TaskMetrics.api.timesheet.status.ValidationHandler;
import inc.pabacus.TaskMetrics.api.timesheet.time.TimeLogConnector;
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
  private static TaskConnector xpmTaskHandler = new TaskConnector();
  private static ActivityHandler activityHandler = new ActivityHandler();
  private static AuthenticatorService authenticatorService = new AuthenticatorService();
  private static LeaveService leaveService = new LeaveService();
  private static StatusUpdateHandler statusUpdateHandler = new StatusUpdateHandler();
  private static ValidationHandler validationHandler = new ValidationHandler();
  private static TimeLogConnector timeLogConnector = new TimeLogConnector();
  private static CacheService cacheService = new StringCacheService();

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

  public static TaskConnector xpmTaskHandler() {
    return xpmTaskHandler;
  }

  public static ActivityHandler activityHandler() {
    return activityHandler;
  }

  public static AuthenticatorService tokenService() {
    return authenticatorService;
  }

  public static LeaveService leaveService() {
    return leaveService;
  }

  public static StatusUpdateHandler statusUpdateHandler() {
    return statusUpdateHandler;
  }

  public static TimeLogConnector timeLogConnector() {
    return timeLogConnector;
  }

  public static CacheService cacheService() {
    return cacheService;
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
