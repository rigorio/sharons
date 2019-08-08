package inc.pabacus.TaskMetrics.api.screenshot;

import inc.pabacus.TaskMetrics.utils.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ScreenshotServiceImpl implements ScreenshotService {

  private static final Logger logger = Logger.getLogger(ScreenshotServiceImpl.class);
  private static final long DEFAULT_INTERVAL = 300;
  private ScheduledExecutorService executorService;
  private ScheduledFuture<?> scheduledFuture;
  private Runnable run = this::takeScreenShot;
  private static final String dir = ".pabacus";
  private ScreenshotConnector connector;

  public ScreenshotServiceImpl() {
    connector = new ScreenshotConnector();
  }

  @Override
  public String takeScreenShot() {
    final String defaultName = LocalDateTime.now().withNano(0).toString();
    return screenshot(defaultName);
  }

  @Override
  public String takeScreenshot(String name) {
    return screenshot(name);
  }

  @Override
  public void enableScreenShot() {
    enableScreenShot(DEFAULT_INTERVAL);
  }

  @Override
  public void enableScreenShot(long interval) {
    executorService = Executors.newSingleThreadScheduledExecutor();
    scheduledFuture = executorService.scheduleAtFixedRate(run, 0, interval, TimeUnit.SECONDS);
  }

  @Override
  public void disableScreenshot() {
    scheduledFuture.cancel(true);
  }

  // not sure if this should be included as an option
  // TODO research about shutdown()
  @Override
  public void shutdownScheduler() {
    executorService.shutdown();
  }

  @Override
  public void sendScreenshot(File image) {
    // TODO
  }

  /**
   * TODO
   * can be made public?
   */
  public List<Path> getScreenshots() throws IOException {
    return Files.walk(Paths.get(FileUtils.tmpFile(dir).getAbsolutePath()))
        .filter(Files::isRegularFile)
        .collect(Collectors.toList());
  }

  private String screenshot(String name) {

    String fileName = name + ".png";
    String path = dir + File.separator + fileName;

    File file = FileUtils.tmpFile(path);
    file.mkdirs();
    try {
      file.createNewFile();
      ScreenshotTool tool = new ScreenshotTool();
      BufferedImage image = tool.blurredScreenshot();
      tool.saveImage(image, "png", file);
      connector.uploadFile(file);
      // retrieve image, send to assets
    } catch (AWTException | IOException e) {
      logger.warn(e.getMessage());
    }
    return file.getAbsolutePath();
  }

}
