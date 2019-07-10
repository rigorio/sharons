package inc.pabacus.TaskMetrics.utils.what;

import com.google.common.hash.Hashing;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Extracted "settings" for manipulating flat files
 *
 * @see DefaultSettings
 */
public interface FlatFileSettings {

  /**
   * Windows uses `\` while Unix based systems use `/`,
   * and they have different `temp` directories
   * so they can't be defined explicitly.
   */
  String s = File.separator;
  String tmpDir = System.getProperty("java.io.tmpdir");

  /**
   * Returns the filename
   *
   * @return
   */
  String getFileName();

  /**
   * Where to write the data to
   *
   * @param fileName
   */
  void setFileName(String fileName);

  /**
   * Does whatever it wants. This was mainly implemented for encryption,
   * so it will be useful if a different kind of encryption is required
   *
   * @param message
   * @return
   */
  String transformMessage(String message);

  /**
   * Writes the content to the file
   *
   * @param content
   * @param fileName
   */
  void write(String content, String fileName);

  /**
   * This is used to retrieve and/or write to the file
   * Override this if you want to change the
   * default directory which is the temp directory
   * <p>
   * Though do be warned that there may be errors
   * encountered if the directory you set does not exist
   *
   * @param fileName file to be retrieved/written to
   * @return
   */
  default File getFile(String fileName) {
    String dirName = getDirName();
    String pathToDir = getFilePath(dirName);
    String absoluteFileName = getAbsoluteFileName(pathToDir, fileName);
    return new File(absoluteFileName);
  }

  @NotNull
  default String getAbsoluteFileName(String pathToDir, String file) {
    return pathToDir + s + file;
  }

  @NotNull
  default String getFilePath(String dirName) {
    String filePath = getDir(dirName);
    createDirectory(filePath);
    return filePath;
  }

  /**
   * Currently returns the temp directory by default
   * <p>
   * Overriding this supposedly allows you to change
   * the directory to which the file is saved
   *
   * @param dirName
   * @return
   */
  @NotNull
  default String getDir(String dirName) {
    String tmpPath = hashString(dirName);
    return tmpDir + tmpPath + s;
  }

  @NotNull
  default String hashString(String string) {
    return s + Hashing.sha256()
        .hashString(string, StandardCharsets.UTF_8)
        .toString();
  }

  default boolean createDirectory(String filePath) {
    return new File(filePath).mkdirs();
  }

  @NotNull
  default String getDirName() {
    return "workbench";
  }
}
