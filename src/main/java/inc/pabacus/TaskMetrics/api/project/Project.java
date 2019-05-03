package inc.pabacus.TaskMetrics.api.project;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
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
  private List<ProjectTask> tasks;

  public Project(ProjectFXAdapter project) {

    LongProperty id = project.getId();
    this.id = id != null ? id.get() : null;

    StringProperty projectName = project.getProjectName();
    this.projectName = projectName != null ? projectName.get() : null;

    DoubleProperty billable = project.getBillable();
    this.billable = billable != null ? billable.get() : null;

    DoubleProperty nonBillable = project.getNonBillable();
    this.nonBillable = nonBillable != null ? nonBillable.get() : null;

    DoubleProperty totalHours = project.getTotalHours();
    this.totalHours = totalHours != null ? totalHours.get() : null;

    DoubleProperty percentBillable = project.getPercentBillable();
    this.percentBillable = percentBillable != null ? percentBillable.get() : null;

    DoubleProperty billRate = project.getBillRate();
    this.billRate = billRate != null ? billRate.get() : null;

    DoubleProperty invoiceAmount = project.getInvoiceAmount();
    this.invoiceAmount = invoiceAmount != null ? invoiceAmount.get() : null;

    this.tasks = project.getProjectTasks();

  }
}
