package inc.pabacus.TaskMetrics.api.software;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SoftwareDataFXAdapter {

  private StringProperty name;
  private StringProperty version;
  private StringProperty dateInstalled;

  public SoftwareDataFXAdapter(SoftwareData softwareData) {
    this.name = new SimpleStringProperty(softwareData.getName());
    this.version = new SimpleStringProperty(softwareData.getVersion());
    this.dateInstalled = new SimpleStringProperty(softwareData.getDateInstalled());
  }
}
