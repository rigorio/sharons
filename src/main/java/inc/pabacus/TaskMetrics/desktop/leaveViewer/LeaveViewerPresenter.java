package inc.pabacus.TaskMetrics.desktop.leaveViewer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.leave.Leave;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaveViewerPresenter implements Initializable {

  private static final String APPROVED = "Approved";
  private static final String DENIED = "Denied";
  @FXML
  private Label statusLabel;
  @FXML
  private JFXTextField requestTypeField;
  @FXML
  private JFXTextField startDateField;
  @FXML
  private JFXTextField endDatefield;
  @FXML
  private JFXTextArea reasonField;
  @FXML
  private JFXButton approveButton;
  @FXML
  private JFXButton denyButton;

  private Leave leave;
  private LeaveService leaveService;

  public LeaveViewerPresenter() {
    leave = LeaveHolder.getLeave();
    leaveService = BeanManager.leaveService();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setValues();
    denyButton.getScene().getWindow().setOnCloseRequest(e -> emptyLeaveHolder());
  }

  private void setValues() {
    statusLabel.setText(leave.getStatus());
    requestTypeField.setText(leave.getTypeOfRequest());
    requestTypeField.setDisable(true);
    startDateField.setText(leave.getStartDate());
    startDateField.setDisable(true);
    endDatefield.setText(leave.getEndDate());
    endDatefield.setDisable(true);
    reasonField.setText(leave.getReason());
    reasonField.setDisable(true);
  }

  @FXML
  public void approveRequest() {
    leave.setStatus(APPROVED);
    updateLeave();
    close();
  }

  @FXML
  public void denyRequest() {
    leave.setStatus(DENIED);
    updateLeave();
    close();
  }

  private void updateLeave() {
    leaveService.saveLeave(leave);
  }

  private void close() {
    emptyLeaveHolder();
    Stage stage = (Stage) denyButton.getScene().getWindow();
    stage.close();
  }

  private void emptyLeaveHolder() {
    LeaveHolder.setLeave(null);
  }
}
