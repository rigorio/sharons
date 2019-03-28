package inc.pabacus.TaskMetrics.api.screenshot;

import java.io.File;
import java.io.IOException;

/**
 *
 * @see ScreenshotServiceImpl
 */
public interface ScreenshotService {

  String takeScreenShot();

  String takeScreenshot(String name);

  void enableScreenShot();

  void enableScreenShot(long interval);

  void disableScreenshot();

  // not sure if this should be included as an option
  // TODO research about shutdown()
  void shutdownScheduler();

  void sendScreenshot(File image) throws IOException;
}
