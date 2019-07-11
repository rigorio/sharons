package inc.pabacus.TaskMetrics.utils.file;

import java.security.InvalidKeyException;

public class DefaultSettings implements FlatFileSettings {

  private FileEncrypterDecrypter fileEncrypterDecrypter;
  private String fileName;

  public DefaultSettings() {
    fileEncrypterDecrypter = FileEncrypterDecrypter.defaultInstance();
  }

  @Override
  public String getFileName() {   // I definitely could have just set a default value
    if (fileName == null)         // But I'm practicing my knowledge in GC and Memory leaks
      return "config.json";       // It ain't much, but using a default overrideable value
    return fileName;              // will result in a minor memory leak ( I THINK )
  }

  @Override
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public String transformMessage(String message) {
    return fileEncrypterDecrypter.decrypt(message);
  }

  @Override
  public void write(String content, String fileName) {
    try {
      fileEncrypterDecrypter.encrypt(content, fileName);
    } catch (InvalidKeyException e) {
      // log this nigga
    }
  }


}
