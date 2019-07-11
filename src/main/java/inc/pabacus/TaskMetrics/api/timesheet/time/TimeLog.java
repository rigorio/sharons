package inc.pabacus.TaskMetrics.api.timesheet.time;

import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeLog {

  private Long id;
  private Long userId;
  private String date;
  private String time;
  private String status;

  public TimeLog(TimeLogFXAdapter timeLog) {

    LongProperty id = timeLog.getId();
    this.id = id != null ? id.get() : null;

    LongProperty userId = timeLog.getUserId();
    this.userId = userId != null ? userId.get() : null;

    StringProperty date = timeLog.getDate();
    this.date = date != null ? date.get() : null;

    StringProperty time = timeLog.getTime();
    this.time = time != null ? time.get() : null;

    StringProperty status = timeLog.getStatus();
    this.status = status != null ? status.get() : null;

  }
}
