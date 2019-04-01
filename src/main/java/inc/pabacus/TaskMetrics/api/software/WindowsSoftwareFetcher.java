package inc.pabacus.TaskMetrics.api.software;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WindowsSoftwareFetcher implements SoftwareFetcher<List<SoftwareData>> {

  @Override
  public List<SoftwareData> fetchSoftware() {
    String command = "Get-ItemProperty HKLM:\\Software\\Wow6432Node\\Microsoft\\"
        + "Windows\\CurrentVersion\\Uninstall\\* | Select-Object DisplayName, "
        + "DisplayVersion, InstallDate | ConvertTo-Json";
    List<SoftwareData> list = new ArrayList<>();
    try {
      String jsonString = runCommand(command);
      ObjectMapper mapper = new ObjectMapper();
      list = mapper.readValue(jsonString, new TypeReference<List<SoftwareData>>() {});
      list.removeIf(softwareData -> softwareData.getDateInstalled() == null &&
          softwareData.getName() == null &&
          softwareData.getVersion() == null);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  private String runCommand(String commandString) throws IOException {
    Process proc = Runtime.getRuntime().exec(new String[]{"powershell", commandString});
    try (Scanner s = new Scanner(new InputStreamReader(proc.getInputStream()))) {
      Scanner scanner = s.useDelimiter("\\A"); // possibly faster than
      if (scanner.hasNext())
        return scanner.next();
    }

    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    }
    proc.destroy();
    return sb.toString();
  }
}

