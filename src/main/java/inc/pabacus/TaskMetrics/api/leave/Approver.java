package inc.pabacus.TaskMetrics.api.leave;

import inc.pabacus.TaskMetrics.desktop.support.ApproverAdapter;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Approver {

  private Long approverId;
  private String approver;
  private String status;

  public Approver() {
  }

  public Approver(ApproverAdapter approverAdapter) {
    LongProperty id = approverAdapter.getId();
    this.approverId = id != null ? id.getValue() : null;

    StringProperty approver = approverAdapter.getApprover();
    this.approver = approver != null ? approver.get() : null;

    StringProperty status = approverAdapter.getStatus();
    this.status = status != null ? status.get() : null;
  }

  public Approver(Long approverId, String approver, String status) {
    this.approverId = approverId;
    this.approver = approver;
    this.status = status;
  }

  public Long getApproverId() {
    return approverId;
  }

  public void setApproverId(Long approverId) {
    this.approverId = approverId;
  }

  public String getApprover() {
    return approver;
  }

  public void setApprover(String approver) {
    this.approver = approver;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
