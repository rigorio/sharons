package inc.pabacus.TaskMetrics.api.tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreationDTO {
  private String task;
  private String description;
  private long jobId;
  private double estimatedTime;
  private long userId;
  private boolean createTemplate;
  private Long jobTaskId;
}
