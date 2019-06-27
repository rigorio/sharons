package inc.pabacus.TaskMetrics.desktop.leaveViewer;

import inc.pabacus.TaskMetrics.api.leave.Leave;
import lombok.ToString;

@ToString
public class LeaveHolder {
  private static Leave leave;

  public static Leave getLeave() {
    return leave;
  }

  public static void setLeave(Leave leave) {
    LeaveHolder.leave = leave;
  }
}
