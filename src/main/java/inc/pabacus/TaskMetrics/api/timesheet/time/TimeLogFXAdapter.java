package inc.pabacus.TaskMetrics.api.timesheet.time;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeLogFXAdapter {
  private LongProperty id;
  private LongProperty userId;
  private StringProperty date;
  private StringProperty time;
  private StringProperty status;

  public TimeLogFXAdapter(TimeLog timeLog) {
    Long id = timeLog.getId();
    this.id = id != null ? new SimpleLongProperty(id) : null;

    Long userId = timeLog.getUserId();
    this.userId = userId != null ? new SimpleLongProperty(userId) : null;

    String date = timeLog.getDate();
    this.date = date != null ? new SimpleStringProperty(date) : null;

    String time = timeLog.getTime();
    this.time = time != null ? new SimpleStringProperty(time) : null;

    String status = timeLog.getStatus();
    this.status = status != null ? new SimpleStringProperty(status) : null;
  }
}
