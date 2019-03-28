package inc.pabacus.TaskMetrics.api.screenshot;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ScreenshotServiceImpl implements ScreenshotService {

  private static final long DEFAULT_INTERVAL = 300;
  private final ScheduledExecutorService executorService;
  private ScheduledFuture<?> scheduledFuture;
  private Runnable run = this::takeScreenShot;
  private static final String dir = ".pabacus";


  public ScreenshotServiceImpl() {
    executorService = Executors.newSingleThreadScheduledExecutor();
  }

  @Override
  public String takeScreenShot() {
    final String defaultName = LocalDateTime.now().withNano(0).toString().replaceAll(":", "");
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

  private String screenshot(String name) {

    String fileName = name + ".png";
    String path = dir + File.separator + fileName;

    File file = new File(System.getProperty("user.home") + File.separator + path);
    file.mkdirs();
    try {
      file.createNewFile();
      Long e = System.nanoTime();

      int radius = 11;
      int size = radius * 2 + 1;
      float weight = 1.0f / (size * size);
      float[] data = new float[size * size];
      for (int i = 0; i < data.length; i++) {
        data[i] = weight;
      }

      Kernel kernel = new Kernel(size, size, data);
      ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
      BufferedImage bufferedImage = op.filter(new Robot()
                                                  .createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())), null);
      System.out.println("bench " + (System.nanoTime() - e));
      BufferedImage image = bufferedImage;
      ImageIO.write(image, "png", file);
      // retrieve image, send to assets
    } catch (AWTException | IOException e) {
      // log
      e.printStackTrace();
    }
    return file.getAbsolutePath();
  }

}
