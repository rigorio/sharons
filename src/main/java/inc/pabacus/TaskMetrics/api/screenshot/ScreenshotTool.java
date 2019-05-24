package inc.pabacus.TaskMetrics.api.screenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

public class ScreenshotTool {

  public BufferedImage blurredScreenshot() throws AWTException {
    BufferedImage bufferedImage = blurImage(screenshot());
    return bufferedImage;
  }

  public BufferedImage screenshot() throws AWTException {
    Rectangle screenRectangular = new Rectangle(0, 0, 0, 0);
    for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      screenRectangular = screenRectangular.union(gd.getDefaultConfiguration().getBounds());
    }
    return new Robot()
        .createScreenCapture(screenRectangular);
  }

  public BufferedImage blurImage(BufferedImage image) {
    int radius = 3;
    int size = radius * 2 + 1;
    float weight = 1.0f / (size * size);
    float[] data = new float[size * size];
    for (int i = 0; i < data.length; i++) {
      data[i] = weight;
    }

    Kernel kernel = new Kernel(size, size, data);
    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    return op.filter(image, null);
  }

  public void saveImage(BufferedImage image, String formatName, File file) throws IOException {
    ImageIO.write(image, formatName, file);
  }

}
