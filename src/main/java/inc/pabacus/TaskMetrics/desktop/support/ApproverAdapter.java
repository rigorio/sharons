package inc.pabacus.TaskMetrics.desktop.support;

import inc.pabacus.TaskMetrics.api.leave.Approver;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproverAdapter {

  private LongProperty id;
  private StringProperty approver;
  private StringProperty status;

  public ApproverAdapter(Approver approver) {
    Long approverId = approver.getApproverId();
    this.id = approverId != null ? new SimpleLongProperty(approverId) : null;

    String approver1 = approver.getApprover();
    this.approver = approver1 != null ? new SimpleStringProperty(approver1) : null;

    String status = approver.getStatus();
    this.status = status != null ? new SimpleStringProperty(status) : null;
  }
}
