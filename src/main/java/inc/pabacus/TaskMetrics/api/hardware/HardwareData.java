package inc.pabacus.TaskMetrics.api.hardware;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class HardwareData {

  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String type;
  private String serialNumber;
  private String manufacturer;
  @Nullable
  private List<Property> properties;

}
