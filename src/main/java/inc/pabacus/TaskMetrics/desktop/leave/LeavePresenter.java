package inc.pabacus.TaskMetrics.desktop.leave;

import com.jfoenix.controls.JFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LeavePresenter implements Initializable {
  @FXML
  private TextArea reason;
  @FXML
  private JFXDatePicker date;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  @FXML
  public void sendForm() {
    String reason = this.reason.getText();
    LocalDate localDate = date.getValue();

    Stage stage = (Stage) this.reason.getScene().getWindow();
    stage.close();
  }
}
