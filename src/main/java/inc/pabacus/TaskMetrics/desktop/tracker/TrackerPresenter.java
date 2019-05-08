package inc.pabacus.TaskMetrics.desktop.tracker;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.TaskFXAdapter;
import inc.pabacus.TaskMetrics.api.tasks.TaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.TaskWebRepository;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
  private TaskFXAdapter selectedTask;

  private Runnable process = () -> {
    long duration = timerService.getTime();
    String time = timerService.formatSeconds(duration);
    timer.setText(time);
  };

  public TrackerPresenter() {
    timerService = new TimerService();
    taskHandler = new TaskHandler(new TaskWebRepository());
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
    String timeSpent = timerService.formatSeconds(time);
    selectedTask.setTotalTimeSpent(new SimpleStringProperty(timeSpent));
    double totalTimeSpent = (double) (time / 3600);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
//    totalTimeSpent = 3;
    selectedTask.setTimeSpent(new SimpleDoubleProperty(totalTimeSpent));
    selectedTask.setStatus(new SimpleStringProperty(Status.DONE.getStatus()));
    selectedTask.setDateCompleted(new SimpleStringProperty(LocalDate.now().toString()));
    Task task = new Task(selectedTask);
    task.setEndTime(formatter.format(LocalTime.now()));
    taskHandler.saveTask(task);
    TrackHandler.setSelectedTask(null);
    ((Stage) complete.getScene().getWindow()).close();
  }
}
