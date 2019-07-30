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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaveViewerPresenter implements Initializable {

  private static final String APPROVED = "Approved";
  private static final String DENIED = "Denied";
  @FXML
  private Label statusLabel;
  @FXML
  private JFXTextField employeeField;
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
//    denyButton.getScene().getWindow().setOnCloseRequest(e -> emptyLeaveHolder());
  }

  private void setValues() {
    String status = leave.getStatus();
    statusLabel.setText(status);

    Color color = status.equals("Approved") ? Color.GREEN : status.equals("Denied") ? Color.RED : Color.MEDIUMPURPLE;
    statusLabel.setTextFill(color);

    employeeField.setText(leave.getEmployeeId());
    setEditable(employeeField);

    requestTypeField.setText(leave.getLeaveTypeId());
    setEditable(requestTypeField);

    startDateField.setText(leave.getStartDate());
    setEditable(startDateField);

    endDatefield.setText(leave.getEndDate());
    setEditable(endDatefield);

    reasonField.setText(leave.getReason());
    reasonField.setEditable(false);
  }

  private void setEditable(JFXTextField txtInput) {
    txtInput.setEditable(false);
    // Uncomment below to disable being able to select text
    // txtInput.setMouseTransparent(true);
    // txtInput.setFocusTraversable(false);
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
    leaveService.updateLeave(leave.getId());
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
