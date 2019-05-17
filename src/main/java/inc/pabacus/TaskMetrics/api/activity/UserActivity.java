package inc.pabacus.TaskMetrics.api.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserActivity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String date;
  private String startTime;
  private String endTime;
  private String activity;

}
