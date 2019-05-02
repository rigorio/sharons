package inc.pabacus.TaskMetrics.api.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

  private Long id;
  private String projectName;
  private Double billable;
  private Double nonBillable;
  private Double totalHours;
  private Double percentBillable;
  private Double billRate;
  private Double invoiceAmount;
  private List<ProjectTask> projectTasks;

}
