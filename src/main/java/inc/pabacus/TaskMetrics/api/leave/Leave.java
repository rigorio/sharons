package inc.pabacus.TaskMetrics.api.leave;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class Leave {
  private Long id;
  private Long userId;
  @OneToMany(cascade = CascadeType.ALL)
  private List<Approver> approver;
  private String startDate;
  private String endDate;
  private String reason;
  private String status;
  private String typeOfRequest;

  public Leave(Long id, Long userId, List<Approver> approver, String startDate, String endDate, String reason, String status, String typeOfRequest) {
    this.id = id;
    this.userId = userId;
    this.approver = approver;
    this.startDate = startDate;
    this.endDate = endDate;
    this.reason = reason;
    this.status = status;
    this.typeOfRequest = typeOfRequest;
  }
}
