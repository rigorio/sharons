package inc.pabacus.TaskMetrics.api.leave;

import inc.pabacus.TaskMetrics.desktop.support.ApproverAdapter;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Approver {

  private Long approverId;
  private String approver;
  private String status;

  public Approver(ApproverAdapter approverAdapter) {
    LongProperty id = approverAdapter.getId();
    this.approverId = id != null ? id.getValue() : null;

    StringProperty approver = approverAdapter.getApprover();
    this.approver = approver != null ? approver.get() : null;

    StringProperty status = approverAdapter.getStatus();
    this.status = status != null ? status.get() : null;
  }
}
