package inc.pabacus.TaskMetrics.desktop.timesheet.hris;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties

public class HRISTimeLog {
  private String date;
  @JsonProperty("timeLog")
  private String time;
  @JsonProperty("timeLogTypeName")
  private String status;
}

/*
                "employeeId": "f6befdfe-8876-4b6a-f503-08d704e3effb",
                "timeLog": "2019-07-23T07:56:01",
                "timeLogTypeId": 1,
                "ipAddress": null,
                "longitude": null,
                "latitude": null,
                "employeeCompanyId": "01-00002",
                "employeeName": "Sarmiento, Rigo",
                "timeLogTypeName": "Time In",
                "timeLogStatus": 1,
                "id": 89
 */
