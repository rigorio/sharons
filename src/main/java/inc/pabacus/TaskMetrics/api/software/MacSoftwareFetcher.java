package inc.pabacus.TaskMetrics.api.software;

import java.util.ArrayList;
import java.util.List;

public class MacSoftwareFetcher implements SoftwareFetcher<List<SoftwareData>> {
  @Override
  public List<SoftwareData> fetchSoftware() {
    return new ArrayList<>();
  }
}
