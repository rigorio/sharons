package inc.pabacus.TaskMetrics.desktop.tracker;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
  private ActivityHandler activityHandler;
  private DailyLogService dailyLogHandler;
  private String startTime;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
  private double timeCompensation = 0;

  public TrackerPresenter() {
    timerService = new TimerService();
    xpmTaskWebHandler = new XpmTaskWebHandler();
    activityHandler = BeanManager.activityHandler();
    dailyLogHandler = BeanManager.dailyLogService();
    process = this::tickTime;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    timer.setText(STARTING_TIME);
    selectedTask = TrackHandler.getSelectedTask();
    timerService.setFxProcess(process);
    timerService.start();
    String taskTitle = selectedTask.getTask().get();
    title.setText(taskTitle);
    startTime = getCurrentTime();
  }

  @FXML
  public void completeTask() {
    selectedTask.setEndTime(new SimpleStringProperty(getCurrentTime()));
    updateTask(Status.DONE.getStatus());
    saveAndClose();
  }

  @FXML
  public void cancel() {
    closeWindow();
  }

  private void saveAndClose() {
    XpmTask xpmTask = new XpmTask(selectedTask);
    if (xpmTask.getStartTime() == null)
      xpmTask.setStartTime(startTime);
    xpmTaskWebHandler.save(xpmTask);
    closeWindow();
  }

  private void tickTime() {
    Stage stage = (Stage) title.getScene().getWindow();
    stage.setAlwaysOnTop(AlwaysOnTopCheckerConfiguration.isAlwaysOnTop());
    long duration = timerService.getTime();
    String time = timerService.formatSeconds(duration);
    timer.setText(time);
  }

  private void closeWindow() {
    TrackHandler.setSelectedTask(null);
    ((Stage) title.getScene().getWindow()).close();
  }

  private void updateTask(String status) {
    // I might use this in the future don't touch because I'm forgetful
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
    String totalTimeSpent = getTotalTimeSpent();
    String currentTime = selectedTask.getTotalTimeSpent().get();
    totalTimeSpent = String.valueOf((Double.parseDouble(currentTime) + Double.parseDouble(totalTimeSpent)));
    selectedTask.setTotalTimeSpent(new SimpleStringProperty(totalTimeSpent));
    selectedTask.setStatus(new SimpleStringProperty(status));
  }

  private String getTotalTimeSpent() {
    long timeInSeconds = timerService.getTime();
    timerService.pause();
    double rawComputedTime = timeInSeconds / ONE_HOUR;
    rawComputedTime += timeCompensation;
    return roundOffDecimal(rawComputedTime);
  }

  private String roundOffDecimal(double totalTimeSpent) {
    // DecimalFormat df = new DecimalFormat("0.00");
    // double t = Double.parseDouble(df.format(totalTimeSpent));
    // This method is faster than using DecimalFormat and parseDouble
    return String.format("%.2f", totalTimeSpent);
  }

  private String getCurrentTime() {
    return formatter.format(LocalTime.now());
  }

  public void pause() {
    String testing = "Testing a feature";
    String development = "Development causes";

    List<String> choices = new ArrayList<>();
    choices.add("Break");
    choices.add("Lunch");
    choices.add("Will work on different task");
    choices.add(testing);
    choices.add(development);
    choices.add("Meeting"); // TODO turn off activity listening dailyLogHandler when on a break

    ChoiceDialog<String> dialog = new ChoiceDialog<>("Select a reason", choices);
    dialog.initStyle(StageStyle.UNDECORATED);
    dialog.setHeaderText("Please select a reason for putting this task on pause");
    dialog.setContentText("Reasons");
    dialog.showAndWait().ifPresent(reason -> {
      Activity activity;
      if (reason.equals("Lunch") || reason.equals("Break")) {
        activity = Activity.BREAK;
        if (reason.equals("Lunch"))
          dailyLogHandler.changeLog(LogStatus.LB.getStatus());
      } else if (reason.equals("Meeting"))
        activity = Activity.MEETING;
      else
        activity = Activity.BUSY;
      timeCompensation = reason.equals(testing) ? 0.3 : reason.equals(development) ? 0.5 : 0.0;
      activityHandler.saveActivity(activity);
      updateTask(Status.IN_PROGRESS.getStatus());
      saveAndClose();
    });
  }
}
