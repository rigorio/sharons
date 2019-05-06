package inc.pabacus.TaskMetrics.api.project;

import javafx.beans.property.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFXAdapter {

  private LongProperty id;
  private StringProperty projectName;
  private DoubleProperty billable;
  private DoubleProperty nonBillable;
  private DoubleProperty totalHours;
  private DoubleProperty percentBillable;
  private DoubleProperty invoiceAmount;
  private List<ProjectTask> projectTasks;

  public ProjectFXAdapter(Project project) {

    Long id = project.getId();
    this.id = id != null ? new SimpleLongProperty(id) : null;

    String projectName = project.getProjectName();
    this.projectName = projectName != null ? new SimpleStringProperty(projectName) : null;

    Double billable = project.getBillable();
    this.billable = billable != null ? new SimpleDoubleProperty(billable) : null;

    Double nonBillable = project.getNonBillable();
    this.nonBillable = nonBillable != null ? new SimpleDoubleProperty(nonBillable) : null;

    Double totalHours = project.getTotalHours();
    this.totalHours = totalHours != null ? new SimpleDoubleProperty(totalHours) : null;

    Double percentBillable = project.getPercentBillable();
    this.percentBillable = percentBillable != null ? new SimpleDoubleProperty(percentBillable) : null;

    Double invoiceAmount = project.getInvoiceAmount();
    this.invoiceAmount = invoiceAmount != null ? new SimpleDoubleProperty(invoiceAmount) : null;

    this.projectTasks = project.getTasks();

  }
}
