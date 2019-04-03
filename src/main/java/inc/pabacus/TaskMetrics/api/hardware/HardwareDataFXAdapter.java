package inc.pabacus.TaskMetrics.api.hardware;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HardwareDataFXAdapter {

  private StringProperty name;
  private StringProperty type;

  public HardwareDataFXAdapter(HardwareData hardwareData) {
    this.name = new SimpleStringProperty(hardwareData.getName());
    this.type = new SimpleStringProperty(hardwareData.getType());
  }

}
