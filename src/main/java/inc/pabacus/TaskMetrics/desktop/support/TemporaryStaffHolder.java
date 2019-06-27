package inc.pabacus.TaskMetrics.desktop.support;

import java.util.HashMap;
import java.util.Map;

public class TemporaryStaffHolder {

  private static Map<Long, String> staffs = new HashMap<>();

  public TemporaryStaffHolder() {
    staffs.put(2L, "Rigo Sarmiento");
    staffs.put(3L, "Carlo Montemayor");
    staffs.put(4L, "Rose Mae Cayabyab");
    staffs.put(5L, "Chris Nebril");
  }

  public String getStaff(Long i) {
    return staffs.get(i);
  }

}
