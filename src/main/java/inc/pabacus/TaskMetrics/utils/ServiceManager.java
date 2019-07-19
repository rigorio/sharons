package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.generateToken.TokenService;
import inc.pabacus.TaskMetrics.api.hardware.HardwareServiceAPI;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.kicker.TokenHolder;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.screenshot.ScreenshotServiceImpl;
import inc.pabacus.TaskMetrics.api.software.SoftwareServiceAPI;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.desktop.idle.IdleView;
import javafx.application.Platform;

public class ServiceManager {

  private KickerService kickerService = BeanManager.kickerService();
  private StandupService standupService = BeanManager.standupService();
  private HardwareServiceAPI hardwareServiceAPI = BeanManager.hardwareServiceAPI();
  private SoftwareServiceAPI softwareServiceAPI = BeanManager.softwareServiceAPI();
  private ScreenshotServiceImpl screenshotService = BeanManager.screenshotService();
  private TokenService tokenService = BeanManager.tokenService();
  private ActivityListener activityListener = BeanManager.activityListener();
  private DailyLogHandler dailyLogHandler = BeanManager.dailyLogService();


  public void activate() {
    hardwareServiceAPI.sendHardwareData();
    standupService.runStandup();
    softwareServiceAPI.sendSoftwareData();
    screenshotService.enableScreenShot();
    Runnable runnable = () -> {
      Platform.runLater(() -> GuiManager.getInstance().displayView(new IdleView()));
      activityListener.unListen();
    };
    activityListener.setEvent(runnable);
    activityListener.setInterval(300000);
    activityListener.listen();
  }

  public void deactivate() {

    kickerService.logout(TokenHolder.getToken());
    standupService.close();
    hardwareServiceAPI.cancel();
    softwareServiceAPI.cancel();
    screenshotService.disableScreenshot();
    screenshotService.shutdownScheduler();
    kickerService.stopKicker();
    tokenService.stopToken();
    activityListener.unListen();
  }

}
