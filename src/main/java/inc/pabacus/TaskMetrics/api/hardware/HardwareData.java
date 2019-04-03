package inc.pabacus.TaskMetrics.api.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class HardwareData {

  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String type;
  private String serialNumber;
  private String manufacturer;

  public HardwareData(String name, String type, String serialNumber, String manufacturer) {
    this.name = name;
    this.type = type;
    this.serialNumber = serialNumber;
    this.manufacturer = manufacturer;
  }
}
