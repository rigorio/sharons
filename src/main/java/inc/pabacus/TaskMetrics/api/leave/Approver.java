package inc.pabacus.TaskMetrics.api.leave;

import javax.persistence.Entity;

@Entity
public class Approver {
  private Long approverId;
  private String approver;
  private String status;

  public Approver() {
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
