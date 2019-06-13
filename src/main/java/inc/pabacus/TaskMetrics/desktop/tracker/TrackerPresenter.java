package inc.pabacus.TaskMetrics.desktop.tracker;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTask;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TrackerPresenter implements Initializable {

  @FXML
  private Label title;
  @FXML
  private Label timer;
  @FXML
  private JFXButton complete;

  private static final String STARTING_TIME = "00:00:00";
  private static final double ONE_HOUR = 3600.0;

  private final Runnable process;

  private TimerService timerService;
  private XpmTaskAdapter selectedTask;
  private XpmTaskWebHandler xpmTaskWebHandler;

  public TrackerPresenter() {
    timerService = new TimerService();
    xpmTaskWebHandler = new XpmTaskWebHandler();
    process = this::tickTime;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    timer.setText(STARTING_TIME);
    selectedTask = TrackHandler.getSelectedTask();
    timerService.setFxProcess(process);
    timerService.start();
    String taskTitle = selectedTask.getTitle().get();
    title.setText(taskTitle);
  }

  @FXML
  public void completeTask() {
    updateTask();
    XpmTask xpmTask = new XpmTask(selectedTask);
    xpmTaskWebHandler.save(xpmTask);
    TrackHandler.setSelectedTask(null);
    closeWindow();
  }

  @FXML
  public void cancel() {
    closeWindow();
  }

  private void tickTime() {
    long duration = timerService.getTime();
    String time = timerService.formatSeconds(duration);
    timer.setText(time);
  }

  private void closeWindow() {
    ((Stage) title.getScene().getWindow()).close();
  }

  private void updateTask() {
    // I might use this in the future don't touch because I'm forgetful
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
    String totalTimeSpent = getTotalTimeSpent();
    selectedTask.setTime(new SimpleStringProperty(totalTimeSpent));
    selectedTask.setStatus(new SimpleStringProperty(Status.DONE.getStatus()));
  }

  private String getTotalTimeSpent() {
    long timeInSeconds = timerService.getTime();
    timerService.pause();
    double rawComputedTime = timeInSeconds / ONE_HOUR;
    return roundOffDecimal(rawComputedTime);
  }

  private String roundOffDecimal(double totalTimeSpent) {
    // DecimalFormat df = new DecimalFormat("0.00");
    // double t = Double.parseDouble(df.format(totalTimeSpent));
    // This method is faster than using DecimalFormat and parseDouble
    return String.format("%.2f", totalTimeSpent);
  }
}
