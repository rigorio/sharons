package inc.pabacus.TaskMetrics.desktop.status;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusPresenter implements Initializable {

  @FXML
  private JFXComboBox<String> statuses;
  @FXML
  private TextArea comment;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    statuses.getItems().addAll("In", "Out to Lunch",
                               "Back From Lunch", "Out");

  }

  @FXML
  public void changeStatus() {
    String status = statuses.getValue();
    if (status == null || status.length() == 0)
      return;
    String comment = this.comment.getText();
    // now what to do with thy status and comment? create an API by yourself ??
    // ----------------
    // I want to switch down to the management ladder yeah
    Stage stage = (Stage) statuses.getScene().getWindow();
    stage.close();
  }
}
