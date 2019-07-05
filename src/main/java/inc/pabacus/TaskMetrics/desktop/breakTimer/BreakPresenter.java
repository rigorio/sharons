package inc.pabacus.TaskMetrics.desktop.breakTimer;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerPresenter;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.ResourceBundle;

public class BreakPresenter implements Initializable {

  @FXML
  private Label timerText;
  @FXML
  private ImageView image;
  @FXML
  private JFXButton backOnline;

  private final Runnable process;
  private TimerService timerService;
  private ActivityHandler activityHandler;

  public BreakPresenter() {
    activityHandler = BeanManager.activityHandler();
    timerService = new TimerService();
    process = this::tickTime;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    Image imageView = new Image("/img/break.png");
    image.setImage(imageView);
    image.setFitWidth(150);
    image.setFitHeight(125);

    timerService.setFxProcess(process);
    timerService.start();

  }

  private void tickTime() {
    ActivityHandler activityHandler = new ActivityHandler();
    long duration = timerService.getTime();
    String time = timerService.formatSeconds(duration);

    timerText.setText(time);
    //13 minutes
    if (activityHandler.getLastLog().equalsIgnoreCase("break")) {
      if (duration == 780) {
        timerText.setStyle("-fx-text-fill: red");
      }
    } else if (activityHandler.getLastLog().equalsIgnoreCase("lunch")){
      if (duration == 3300) { // 55 minutes
        timerText.setStyle("-fx-text-fill: red");
      }
    }
  }

  @FXML
  private void backOnline() {
    activityHandler.saveActivity(Activity.ONLINE);
    notification("Online");
    timerService.pause();
    DailyLogHandler dailyLogHandler = new DailyLogHandler();
    dailyLogHandler.checkIfUserIsBreak();
    TrackerPresenter.isContinue = true;
    Stage stage = (Stage) backOnline.getScene().getWindow();
    stage.close();
  }

  private void notification(String notif) {
    Notifications notifications = Notifications.create()
        .title("TRIBELY")
        .text("Status change to " + notif)
        .position(Pos.BOTTOM_RIGHT)
        .hideAfter(Duration.seconds(5));
    notifications.darkStyle();
    notifications.showWarning();
  }
}
