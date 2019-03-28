package inc.pabacus.TaskMetrics.utils;

import java.io.File;

public class FileUtils {

  private static final String SEPARATOR = File.separator;
  private static final String TMP_DIR = System.getProperty("user.home");

  public static File tmpFile(String path) {
    return new File(TMP_DIR + SEPARATOR + path);
  }

}
