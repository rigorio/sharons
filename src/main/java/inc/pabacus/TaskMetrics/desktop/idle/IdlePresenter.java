package inc.pabacus.TaskMetrics.desktop.idle;

import com.jfoenix.controls.JFXTextArea;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.UserActivity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class IdlePresenter implements Initializable {

  @FXML
  private JFXTextArea activityArea;

  private ActivityHandler activityHandler;

  private String startTime;

  private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    activityHandler = new ActivityHandler();
    startTime = timeFormatter.format(LocalTime.now());
  }

  @FXML
  public void submitActivity() {
    String activity = activityArea.getText();
    UserActivity userActivity = UserActivity.builder()
        .startTime(startTime)
        .date(LocalDate.now().toString())
        .endTime(timeFormatter.format(LocalTime.now()))
        .activity(activity)
        .build();
    activityHandler.saveActivity(userActivity);
    Stage stage = (Stage) activityArea.getScene().getWindow();
    stage.close();
  }
}