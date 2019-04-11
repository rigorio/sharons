package inc.pabacus.TaskMetrics.api.tasks;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskLog extends RecursiveTreeObject<TaskLog> {
  private StringProperty date;
  private StringProperty title;

  public TaskLog(String date, String title) {
    this.date = new SimpleStringProperty(date);
    this.title = new SimpleStringProperty(title);
  }
}
