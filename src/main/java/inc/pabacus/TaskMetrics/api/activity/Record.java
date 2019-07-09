package inc.pabacus.TaskMetrics.api.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Record {
  private RecordType recordType;
  private String activity;
  private Double duration;
}
