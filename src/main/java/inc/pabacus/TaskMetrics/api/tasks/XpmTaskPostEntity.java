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
  private String dateCreated;
  private String status;
  private String description;
  private String totalTimeSpent;
  private Boolean billable;
  private String startTime;
  private String endTime;
  private String estimateTime;
  private String extendCounter;
  private String percentCompleted;
  private String dateFinished;
  private Long taskId;
  private Long jobId;
  private Long clientId;
  private Long jobTasksId;


}