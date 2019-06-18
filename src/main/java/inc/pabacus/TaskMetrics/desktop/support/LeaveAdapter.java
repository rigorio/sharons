package inc.pabacus.TaskMetrics.desktop.support;

import inc.pabacus.TaskMetrics.api.leave.Leave;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveAdapter {

  private LongProperty id;
  private LongProperty userId;
  private StringProperty startDate;
  private StringProperty endDate;
  private StringProperty reason;
  private StringProperty status;
  private StringProperty typeOfRequest;

  public LeaveAdapter(Leave leave) {

    Long id = leave.getId();
    this.id = id != null ? new SimpleLongProperty(id) : null;

    Long userId = leave.getUserId();
    this.userId = userId != null ? new SimpleLongProperty(userId) : null;

    String startDate = leave.getStartDate();
    this.startDate = startDate != null ? new SimpleStringProperty(startDate) : null;

    String endDate = leave.getEndDate();
    this.endDate = endDate != null ? new SimpleStringProperty(endDate) : null;

    String reason = leave.getReason();
    this.reason = reason != null ? new SimpleStringProperty(reason) : null;

    String status = leave.getStatus();
    this.status = status != null ? new SimpleStringProperty(status) : null;

    String typeOfRequest = leave.getTypeOfRequest();
    this.typeOfRequest = typeOfRequest != null ? new SimpleStringProperty(typeOfRequest) : null;

  }
}
