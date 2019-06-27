package inc.pabacus.TaskMetrics.api.leave;

import inc.pabacus.TaskMetrics.desktop.support.ApproverAdapter;
import inc.pabacus.TaskMetrics.desktop.support.LeaveAdapter;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
public class Leave {
  private Long id;
  private Long userId;
  @OneToMany(cascade = CascadeType.ALL)
  private List<Approver> approvers;
  private String startDate;
  private String endDate;
  private String reason;
  private String status;
  private String typeOfRequest;

  public Leave(LeaveAdapter leaveAdapter) {
    LongProperty id = leaveAdapter.getId();
    this.id = id != null ? id.get() : null;

    LongProperty userId = leaveAdapter.getUserId();
    this.userId = userId != null ? userId.getValue() : null;

    List<ApproverAdapter> approver = leaveAdapter.getApprovers();
    this.approvers = approver != null
        ? new ArrayList<>(approver.stream()
                              .map(Approver::new)
                              .collect(Collectors.toList()))
        : null;

    StringProperty startDate = leaveAdapter.getStartDate();
    this.startDate = startDate != null ? startDate.get() : null;

    StringProperty endDate = leaveAdapter.getEndDate();
    this.endDate = endDate != null ? endDate.getValue() : null;

    StringProperty reason = leaveAdapter.getReason();
    this.reason = reason != null ? reason.getValue() : null;

    StringProperty status = leaveAdapter.getStatus();
    this.status = status != null ? status.get() : null;

    StringProperty typeOfRequest = leaveAdapter.getTypeOfRequest();
    this.typeOfRequest = typeOfRequest != null ? typeOfRequest.getValue() : null;

  }
}
