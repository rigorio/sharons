package inc.pabacus.TaskMetrics.api.tasks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XpmTaskPostEntity {
  private Long id;
  private Long clientId;
  private Long jobId;
  private String description;
  private Long taskId;
  private String status;
  private String dateCreated;
  private String dateFinished;
  private Boolean billable;
  private String startTime;
  private String endTime;
  private String estimateTime;
  private String extendCounter;
  private String totalTimeSpent;
  private String percentCompleted;
  private Long businessValueId;
  private Long invoiceTypeId;
  private Long assigneeId;


}
