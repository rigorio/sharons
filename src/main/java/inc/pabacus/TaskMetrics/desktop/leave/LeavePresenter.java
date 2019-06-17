package inc.pabacus.TaskMetrics.desktop.leave;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import inc.pabacus.TaskMetrics.api.leave.Approver;
import inc.pabacus.TaskMetrics.api.leave.Leave;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LeavePresenter implements Initializable {
  @FXML
  private AnchorPane mainPane;
  @FXML
  private TextArea reason;
  @FXML
  private JFXDatePicker startDate;
  @FXML
  private JFXDatePicker endDate;
  @FXML
  private JFXComboBox request;
  @FXML
  private JFXButton close;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    endDate.requestFocus();
    request.getItems().addAll("Vacation Leave", "Sick Leave", "Maternity Leave", "Unpaid Leave");

    dates();
  }

  @FXML
  public void sendForm() {
    if (isNotEmpty()){
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Error");
      alert.setContentText("Please fill out all the fields");
      alert.showAndWait();
      return;
    }
    submitRequest();
  }

  private void dates(){
    startDate.valueProperty().addListener((ov, oldValue, newValue) -> {
      endDate.setDayCellFactory(picker -> new DateCell() {
        public void updateItem(LocalDate date, boolean empty) {
          super.updateItem(date, empty);
          setDisable(empty || date.compareTo(startDate.getValue()) < 0 );
        }
      });
    });
  }

  private void submitRequest() {
    String requestString = (String) this.request.getValue();
    String startDateString = String.valueOf(this.startDate.getValue());
    String endDateString = String.valueOf(this.endDate.getValue());
    String reason = this.reason.getText();
    String status = "Pending";
    double teamLeader = 0;
    double supervisor = 3;
    double manager = 4;

    List<Approver> getApprovers = Arrays.asList(
        new Approver(teamLeader,supervisor,manager));

    LeaveService service = new LeaveService();
    Leave leave = service.requestLeave(new Leave(2L,3L, getApprovers,startDateString,endDateString,reason,status,requestString));
    System.out.println(leave);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Request submitted!");
    alert.showAndWait();
    cancel();
  }

  public void cancel() {
    ((Stage) mainPane.getScene().getWindow()).close();
  }

  private boolean isNotEmpty() {
    return reason.getText().isEmpty() || startDate.getValue() == null || endDate.getValue() == null || request.getValue() == null;
  }

  @FXML
  void close(ActionEvent event) {
    cancel();
  }
}
