package inc.pabacus.TaskMetrics.desktop.breakTimer;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.Record;
import inc.pabacus.TaskMetrics.api.activity.RecordType;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.api.timesheet.handlers.HRISLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.handlers.LogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.desktop.idle.IdleView;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerPresenter;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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
  private LogService logService;
  private ActivityListener activityListener = BeanManager.activityListener();
  public static boolean windowIsOpen = false;
  private String lastLog;

  public BreakPresenter() {
    activityHandler = BeanManager.activityHandler();
    logService = new HRISLogHandler();
    timerService = new TimerService();
    process = this::tickTime;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (TrackerPresenter.windowIsOpen) {
      TrackerPresenter.isPause = true;
    }
//    activityListener.unListen(); //unlisten idle

    Image imageView = new Image("/img/break.png");
    image.setImage(imageView);
    image.setFitWidth(150);
    image.setFitHeight(125);

    timerService.setFxProcess(process);
    timerService.start();
    windowIsOpen = true;

    lastLog = activityHandler.getLastLog();
    Platform.runLater(this::onCloseRequest);

  }

  private void tickTime() {
    long duration = timerService.getTime();
    String time = timerService.formatSeconds(duration);

    timerText.setText(time);
    //13 minutes
    if (lastLog.equalsIgnoreCase("break")) {
      if (duration == 780) {
        timerText.setStyle("-fx-text-fill: red");
      }
    } else if (lastLog.equalsIgnoreCase("lunch") || lastLog.equalsIgnoreCase("lunch break")) {
      if (duration == 3300) { // 55 minutes
        timerText.setStyle("-fx-text-fill: red");
      }
    }
  }

  @FXML
  private void backOnline() {
    String recordActivity = "Break";
    logService.changeLog(LogStatus.BFB.getStatus());
    if (lastLog.equalsIgnoreCase("break")) {
      activityHandler.saveTimestamp(Activity.ONLINE);
      recordActivity = "Break";
      notification("Online");
    } else if (lastLog.equalsIgnoreCase("lunch") || lastLog.equalsIgnoreCase("lunch break")) {
      activityHandler.saveTimestamp(Activity.BFB);
      recordActivity = "Lunch Break";
      notification("Back From Lunch");
    }
    double totalTimeSpent = timerService.getTime() / 3600.0;
    activityHandler.saveRecord(Record.builder()
                                   .recordType(RecordType.BREAK)
                                   .duration(roundOffDecimal(totalTimeSpent))
                                   .activity(recordActivity)
                                   .build());

    timerService.pause();

    if (TrackerPresenter.windowIsOpen) {
      TrackerPresenter.isContinue = true;
    }

    windowIsOpen = false;
    Stage stage = (Stage) backOnline.getScene().getWindow();
    stage.close();
    //reRun Listener
    Runnable runnable = () -> {
      Platform.runLater(() -> GuiManager.getInstance().displayView(new IdleView()));
      activityListener.unListen();
    };
    activityListener.setEvent(runnable);
    activityListener.setInterval(300000);
    activityListener.listen();
  }

  private String roundOffDecimal(double totalTimeSpent) {
    // DecimalFormat df = new DecimalFormat("0.00");
    // double t = Double.parseDouble(df.format(totalTimeSpent));
    // This method is faster than using DecimalFormat and parseDouble
    return String.format("%.2f", totalTimeSpent);
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

  private void onCloseRequest() {
    Stage stage = (Stage) backOnline.getScene().getWindow();
    stage.setOnCloseRequest(evt -> {
      evt.consume();
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Not allowed");
      alert.setContentText("You're not allowed to close this window. Please back online!");
      alert.showAndWait();
    });
  }
}
