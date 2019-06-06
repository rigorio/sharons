package inc.pabacus.TaskMetrics.desktop.tracker;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.tasks.TaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.TaskWebRepository;
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

  private TimerService timerService;
  private TaskHandler taskHandler;
  private XpmTaskAdapter selectedTask;
  private XpmTaskWebHandler xpmTaskWebHandler;

  private Runnable process = () -> {
    long duration = timerService.getTime();
    String time = timerService.formatSeconds(duration);
    timer.setText(time);
  };

  public TrackerPresenter() {
    timerService = new TimerService();
    taskHandler = new TaskHandler(new TaskWebRepository());
    xpmTaskWebHandler = new XpmTaskWebHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    timer.setText("00:00:00");
    selectedTask = TrackHandler.getSelectedTask();
    timerService.setFxProcess(process);
    timerService.start();
    title.setText(selectedTask.getTitle().get());
  }

  @FXML
  public void completeTask() {
    long time = timerService.getTime();
    timerService.pause();
    double jikan = time / 3600.0;
    double totalTimeSpent = jikan;

//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);

    selectedTask.setTime(new SimpleStringProperty("" + totalTimeSpent));
    selectedTask.setStatus(new SimpleStringProperty(Status.DONE.getStatus()));
    XpmTask xpmTask = new XpmTask(selectedTask);
    xpmTaskWebHandler.save(xpmTask);
    TrackHandler.setSelectedTask(null);
    ((Stage) complete.getScene().getWindow()).close();
  }
  @FXML
  public void cancel(){
    Stage stage = (Stage) title.getScene().getWindow();
    stage.close();
  }
}
