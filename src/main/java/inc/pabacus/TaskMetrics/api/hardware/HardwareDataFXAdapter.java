package inc.pabacus.TaskMetrics.api.hardware;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

@Data
public class HardwareDataFXAdapter extends RecursiveTreeObject<HardwareDataFXAdapter> {

  private StringProperty name;
  private StringProperty description;

  public HardwareDataFXAdapter(HardwareData hardwareData) {
    this.name = new SimpleStringProperty(hardwareData.getName());
    this.description = new SimpleStringProperty(hardwareData.getDescription());
  }

}
