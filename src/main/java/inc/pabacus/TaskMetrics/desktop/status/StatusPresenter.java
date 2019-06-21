package inc.pabacus.TaskMetrics.desktop.status;

import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusPresenter implements Initializable {

  @FXML
  private JFXComboBox<String> statuses;
  @FXML
  private TextArea comment;

  private ActivityHandler activityHandler;
  private DailyLogService dailyLogService;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    activityHandler = BeanManager.activityHandler();
    dailyLogService = BeanManager.dailyLogService();
    statuses.getItems().addAll("In", "Out to Lunch",
                               "Back From Lunch", "Out");
  }

  @FXML
  public void changeStatus() {
    String currentStatus = statuses.getValue();
    if (currentStatus == null || currentStatus.length() == 0)
      return;

    if (TrackHandler.getSelectedTask() != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not Allowed");
      alert.setHeaderText(null);
      alert.setContentText("Cannot change activity while tracking a task");
      alert.showAndWait();
      return;
    }

    Activity activity = Activity.BUSY; // default ?

    String status = LogStatus.OUT.getStatus();
    switch (currentStatus) {
      case "Out":
        status = LogStatus.OUT.getStatus();
        activity = Activity.IN;
        break;
      case "In":
        status = LogStatus.IN.getStatus();
        activity = Activity.OUT;
        break;
      case "Out To Lunch":
        status = LogStatus.OTL.getStatus();
        activity = Activity.OTL;
        break;
      case "Back From Lunch":
        status = LogStatus.BFL.getStatus();
        activity = Activity.BFL;
        break;
    }
    dailyLogService.changeLog(status);
    activityHandler.saveActivity(activity);

    String comment = this.comment.getText();
    // now what to do with comment
    Stage stage = (Stage) statuses.getScene().getWindow();
    stage.close();
  }
}
