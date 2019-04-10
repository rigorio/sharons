package inc.pabacus.TaskMetrics.api.timesheet.logs;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyLogFXAdapter extends RecursiveTreeObject<DailyLogFXAdapter> {

  private StringProperty date;
  private StringProperty in;
  private StringProperty otl;
  private StringProperty bfl;
  private StringProperty out;

  public DailyLogFXAdapter(DailyLog dailyLog) {
    date = new SimpleStringProperty(dailyLog.getDate());
    in = new SimpleStringProperty(dailyLog.getIn());
    otl = new SimpleStringProperty(dailyLog.getOtl());
    bfl = new SimpleStringProperty(dailyLog.getBfl());
    out = new SimpleStringProperty(dailyLog.getOtl());
  }
}
