package inc.pabacus.TaskMetrics.api.leave;

import inc.pabacus.TaskMetrics.desktop.support.LeaveAdapter;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Leave {
  private Long id;
  private String employeeId;
  private String status;
  private String reason;
  private Long amount;
  private String startDate;
  private String endDate;
  private String leaveTypeId;

  public Leave(LeaveAdapter leaveAdapter) {
    LongProperty id = leaveAdapter.getId();
    this.id = id != null ? id.get() : null;

    StringProperty employeeId = leaveAdapter.getEmployeeId();
    this.employeeId = employeeId != null ? employeeId.getValue() : null;

    StringProperty status = leaveAdapter.getStatus();
    this.status = status != null ? status.get() : null;

    StringProperty reason = leaveAdapter.getReason();
    this.reason = reason != null ? reason.getValue() : null;

    LongProperty amount = leaveAdapter.getAmount();
    this.amount = amount != null ? amount.get() : null;

    StringProperty startDate = leaveAdapter.getStartDate();
    this.startDate = startDate != null ? startDate.get() : null;

    StringProperty endDate = leaveAdapter.getEndDate();
    this.endDate = endDate != null ? endDate.getValue() : null;

    StringProperty leaveTypeId = leaveAdapter.getLeaveTypeId();
    this.leaveTypeId = leaveTypeId != null ? leaveTypeId.get() : null;

  }
}
