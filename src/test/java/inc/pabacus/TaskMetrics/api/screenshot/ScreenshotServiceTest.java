package inc.pabacus.TaskMetrics.api.screenshot;

import inc.pabacus.TaskMetrics.utils.FileUtils;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertTrue;

public class ScreenshotServiceTest {

  private ScreenshotService screenshotService = new ScreenshotServiceImpl();

  @Test
  public void testScreenshot() {
    String name = "testFile";
    screenshotService.takeScreenshot(name);
    String filePath = FileUtils.tmpFile(".pabacus") + File.separator + name + ".png";
    File file = new File(filePath);
    assertTrue(file.exists());
    file.delete();
  }


}
