package inc.pabacus.TaskMetrics.api.timesheet.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DailyLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String date;
  private String in;
  private String otl;
  private String bfl;
  private String out;

  public DailyLog(String date, String in, String otl, String bfl, String out) {
    this.date = date;
    this.in = in;
    this.otl = otl;
    this.bfl = bfl;
    this.out = out;
  }
}
