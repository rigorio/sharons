package inc.pabacus.TaskMetrics.api.software;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UbuntuSoftwareFetcher implements SoftwareFetcher<List<SoftwareData>> {

  private static final Logger logger = Logger.getLogger(UbuntuSoftwareFetcher.class);

  @Override
  public List<SoftwareData> fetchSoftware() {

    List<SoftwareData> softwares = new ArrayList<>();
    Process fetchInstalledPackagesProcess = null;
    try {
      fetchInstalledPackagesProcess = Runtime.getRuntime().exec("zcat -f grep install /var/log/dpkg.log.2.gz");
    } catch (IOException e) {
      // TODO add logger
    }
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new InputStreamReader(fetchInstalledPackagesProcess.getInputStream()));
      String line;
      while (true) {
        line = reader.readLine();
        if (line == null)
          break;
        String[] lines = line.split(" ");
        if (lines.length > 5) {
          String name = getName(lines);
          String date = lines[0];
          String version = lines[5].equalsIgnoreCase("<none>") ? lines[4] : lines[5];
          softwares.add(new SoftwareData(name, date, version));
        }
      }
    } catch (Exception e) {
      logger.warn(e.getMessage());
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        logger.warn(e.getMessage());
      }
    }
    return softwares;
  }

  private String getName(String[] lines) {
    return lines[5].equalsIgnoreCase("<none>") ? lines[3] : lines[4].equalsIgnoreCase("<none>") ? lines[3] : lines[4];
  }
}
