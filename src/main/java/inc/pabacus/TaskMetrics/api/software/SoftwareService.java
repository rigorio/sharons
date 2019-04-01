package inc.pabacus.TaskMetrics.api.software;

import java.util.List;
import java.util.Set;

/**
 * @see SoftwareHandler
 */
public interface SoftwareService {

  String getOs();

  Set<String> getProcessNames();

  <T> T fetchSoftware(SoftwareFetcher<T> softwareFetcher);

  List<SoftwareData> getSoftware();

}
