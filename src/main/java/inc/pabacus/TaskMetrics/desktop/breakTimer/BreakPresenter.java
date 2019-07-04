package inc.pabacus.TaskMetrics.desktop.breakTimer;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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

  public BreakPresenter(){
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

  private void tickTime(){
    long duration = timerService.getTime();
    String time = timerService.formatSeconds(duration);

    timerText.setText(time);
    //13 minutes
    if(duration == 780){
      timerText.setStyle("-fx-text-fill: red");
    }
  }

  @FXML
  private void backOnline(){
    activityHandler.saveActivity(Activity.ONLINE);
    timerService.pause();
    DailyLogHandler dailyLogHandler = new DailyLogHandler();
    dailyLogHandler.checkIfUserIsBreak();
    Stage stage = (Stage) backOnline.getScene().getWindow();
    stage.close();
  }
}
