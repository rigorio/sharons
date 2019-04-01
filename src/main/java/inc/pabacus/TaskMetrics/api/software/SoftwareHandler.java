package inc.pabacus.TaskMetrics.api.software;

import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SoftwareHandler implements SoftwareService {

  private OperatingSystem os;

  public SoftwareHandler() {
    SystemInfo si = new SystemInfo();
    os = si.getOperatingSystem();
  }

  @Override
  public String getOs() {
    return os.toString();
  }

  @Override
  public Set<String> getProcessNames() {

    return Arrays.stream(os.getProcesses(os.getProcessCount(), OperatingSystem.ProcessSort.CPU))
        .map(OSProcess::getName)
        .collect(Collectors.toSet());
  }

  @Override
  public List<SoftwareData> getSoftware() {
    String os = getOs().toLowerCase();
    if (os.contains("win")) {
      return fetchSoftware(new WindowsSoftwareFetcher());
    } else if (os.contains("ubuntu")) {
      return fetchSoftware(new UbuntuSoftwareFetcher());
    } else if (os.contains("mac")) {
      return fetchSoftware(new MacSoftwareFetcher());
    }
    return new ArrayList<>();
  }

  @Override
  public <T> T fetchSoftware(SoftwareFetcher<T> softwareFetcher) {
    return softwareFetcher.fetchSoftware();
  }

}
