package inc.pabacus.TaskMetrics.api.tasks;

import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class WorkLog {

  private String timeSpent;
  private String date;
  private String status;

  public void logWork(String timeSpent, Status status) {
    this.timeSpent = timeSpent;
    this.status = status.getStatus();
    this.date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
  }

  public String getStatus() {
    return status;
  }

  public String getTimeSpent() {
    return timeSpent;
  }

  public String getDate() {
    return date;
  }

  public void setTimeSpent(String timeSpent) {
    this.timeSpent = timeSpent;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
