package inc.pabacus.TaskMetrics.desktop.leave;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendLeave {
  private String employeeId;
  private String managerEmployeeId;
  private String reason;
  private Long amount;
  private Long leaveTypeId;
  private String startDate;
  private String endDate;
  private String employeeCompanyId;
  private String employeeName;

}
