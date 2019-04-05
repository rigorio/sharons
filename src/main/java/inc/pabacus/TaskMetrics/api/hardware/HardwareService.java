package inc.pabacus.TaskMetrics.api.hardware;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;

/**
 * @see WindowsHardwareHandler
 */
public interface HardwareService {

  default HardwareAbstractionLayer getHal() {
    return new SystemInfo().getHardware();
  }

  List<DiskData> getDisks();

  HardwareAbstractionLayer getAllInfo();

  List<String> getHardwareData(Device device);

  public List<String> getHardwareData(String name);

  List<HardwareData> getDisplays();
}
