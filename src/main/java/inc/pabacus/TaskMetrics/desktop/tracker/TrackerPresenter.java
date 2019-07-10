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
import inc.pabacus.TaskMetrics.desktop.settings.ExtendConfiguration;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TrackerPresenter implements Initializable {

  @FXML
  private Label title;
  @FXML
  private Label timer;
  @FXML
  private JFXButton complete;
  @FXML
  private JFXButton cancel;
  @FXML
  private JFXButton extend;
  @FXML
  private JFXButton pauseButton;
  @FXML
  private JFXButton continueButton;

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
  public ScheduledFuture<?> scheduledFuture;

  public static boolean isPause = false;
  public static boolean isContinue = false;
  public static boolean windowIsOpen = false;

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

    if (CountdownTimerConfiguration.isCountdownTimer()) {
      String getEstimateTime = selectedTask.getEstimateTime().get();
      double estimateTime = Double.parseDouble(getEstimateTime) * 60 * 60;
      timerService.setCountdownProcess(process);
      timerService.setTime((long) estimateTime);
    } else {
      timerService.setFxProcess(process);
    }

    timerService.start();
    String taskTitle = selectedTask.getTask().get();
    title.setText(taskTitle);
    startTime = getCurrentTime();
    extend.setVisible(false);
    continueButton.setVisible(false);
    checkIfContinue();
    checkIfPause();
    windowIsOpen = true;
  }

  @FXML
  private void extend() {
    selectedTask = TrackHandler.getSelectedTask();
    try {
      String extendCounter = selectedTask.getExtendCounter().get();
      long tick = Long.parseLong(extendCounter) + 1;
      selectedTask.setExtendCounter(new SimpleStringProperty(String.valueOf(tick)));
    } catch (Exception e) {
      //in case that ExtendCounter is null
      selectedTask.setExtendCounter(new SimpleStringProperty("1"));
    }
    //get Extended(minutes) * 60 = 1 hour
    long getExtendMinutes = Long.parseLong(ExtendConfiguration.getExtendMinutes()) * 60;
    timerService.setTime(timerService.getTime() + getExtendMinutes);
    String getEstimateTime = selectedTask.getEstimateTime().get();

    double getExtendMinutesConverted = getExtendMinutes / ONE_HOUR;
    //add estimateTime and extendMinutes
    double addBoth = Double.parseDouble(getEstimateTime) + getExtendMinutesConverted;
    //setEstimateTime
    selectedTask.setEstimateTime(new SimpleStringProperty(String.valueOf(addBoth)));
    //to reset the notification
    tenMinutes = 0;
    twoMinutes = 0;
  }

  @FXML
  public void completeTask() {

    List<String> choices = new ArrayList<>();
    choices.add("0%");
    choices.add("10%");
    choices.add("20%");
    choices.add("30%");
    choices.add("40%");
    choices.add("50%");
    choices.add("60%");
    choices.add("70%");
    choices.add("80%");
    choices.add("90%");
    choices.add("100%");

    ChoiceDialog<String> dialog = new ChoiceDialog<>("Select a percentage", choices);
    dialog.initStyle(StageStyle.UNDECORATED);
    dialog.setHeaderText("Please select how many percentage this task has been completed");
    dialog.setContentText("Percentages");
    dialog.showAndWait().ifPresent(reason -> {
      if (reason.equals("100%")) {
        selectedTask.setEndTime(new SimpleStringProperty(getCurrentTime()));
        updateTask(Status.DONE.getStatus());
      } else
        updateTask(Status.IN_PROGRESS.getStatus());
      selectedTask.setPercentCompleted(new SimpleStringProperty(reason));
      saveAndClose();
    });
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

  private int tenMinutes = 1, twoMinutes = 1;

  private void tickTime() {
    Stage stage = (Stage) title.getScene().getWindow();
    stage.setAlwaysOnTop(AlwaysOnTopCheckerConfiguration.isAlwaysOnTop());
    long duration = timerService.getTime();
    String time = timerService.formatSeconds(duration);

    if (CountdownTimerConfiguration.isCountdownTimer()) {
//    timer.setText(time);
      if (duration > 600) {
        timer.setStyle("-fx-text-fill: white");
        timer.setText(time);
        extend.setVisible(false);
        cancel.setVisible(true);
      } else if (duration <= 600 && duration > 120) { //10 minutes in seconds
        timer.setText(time);
        if (tenMinutes == 1)
          notification("10 minutes");
        extend.setVisible(true);
        cancel.setVisible(false);
        tenMinutes += 1;
        timer.setStyle("-fx-text-fill: yellow");
      } else if (duration <= 120 && duration >= 1) { //2 minutes in seconds
        timer.setText(time);
        if (twoMinutes == 1)
          notification("2 minutes");
        extend.setVisible(true);
        cancel.setVisible(false);
        twoMinutes += 1;
        timer.setStyle("-fx-text-fill: red");
      } else if (duration == 0) { //0 timer will stop and then
        completeTask();
        Thread.currentThread().interrupt();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Timer Stopped");
        alert.setContentText("Time's up! Will now complete the task.");
        alert.showAndWait();
        timer.setText(STARTING_TIME);

      } else {
        timer.setText(STARTING_TIME);
      }
//
//    } else if (isPause) {
//      //if Pause timer will re-Run
//      long durations = timerService.getTime();
//      timerService.setTime(durations);
//      String times = timerService.formatSeconds(durations);
//      timer.setText(times);
//      isPause = false;
    } else {
      timer.setText(time);
    }

  }

  private void closeWindow() {
    TrackHandler.setSelectedTask(null);
    timerService.pause();
    ((Stage) title.getScene().getWindow()).close();
    windowIsOpen = false;
  }

  private void updateTask(String status) {
    double currentTime = Double.parseDouble(selectedTask.getTotalTimeSpent().get());
    double totalComputedTime = getRawComputedTime() + currentTime;
    String totalTimeSpent = roundOffDecimal(totalComputedTime);
    selectedTask.setTotalTimeSpent(new SimpleStringProperty(totalTimeSpent));
    selectedTask.setStatus(new SimpleStringProperty(status));
  }

  private double getRawComputedTime() {
    if (CountdownTimerConfiguration.isCountdownTimer()) {
      selectedTask = TrackHandler.getSelectedTask();
      String getEstimateTime = selectedTask.getEstimateTime().get();
      //get the estimateTime and timeInSeconds
      double estimateTime = Double.parseDouble(getEstimateTime);
      long timeInSeconds = timerService.getTime();
      timerService.pause();
      double timeInSecondsInHour = timeInSeconds / ONE_HOUR;
      //subtract estimateTime and TimeInSecondsInHour
      double rawComputedTime = estimateTime - timeInSecondsInHour;
      rawComputedTime += timeCompensation;
      return rawComputedTime;
    } else {
      long timeInSeconds = timerService.getTime();
      timerService.pause();
      double rawComputedTime = timeInSeconds / ONE_HOUR;
      rawComputedTime += timeCompensation;
      return rawComputedTime;
    }

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
      switch (reason) {
        case "Break":
          activity = Activity.BREAK;
          activityHandler.saveActivity(activity);
          timerService.reRun(); // rerun services

//          checkIfContinue();
          continueButton.setVisible(true);
          pauseButton.setVisible(false);
          Stage stage = (Stage) continueButton.getScene().getWindow();
          stage.hide();
//          isPause = true;
          break;
        case "Lunch":
          activity = Activity.LUNCH;
          activityHandler.saveActivity(activity);
          dailyLogHandler.changeLog(LogStatus.LB.getStatus());
          timerService.reRun(); // rerun services

//          checkIfContinue();
          continueButton.setVisible(true);
          pauseButton.setVisible(false);
          Stage stages = (Stage) continueButton.getScene().getWindow();
          stages.hide();
//          isPause = true;
          break;
        case "Meeting":
          activity = Activity.MEETING;
          activityHandler.saveActivity(activity);
          updateTask(Status.IN_PROGRESS.getStatus());
          saveAndClose();
          break;
        default:
          activity = Activity.BUSY;
          timeCompensation = reason.equals(testing) ? 0.3 : reason.equals(development) ? 0.5 : 0.0;
          activityHandler.saveActivity(activity);
          updateTask(Status.IN_PROGRESS.getStatus());
          saveAndClose();
          break;
      }
    });
  }

  @FXML
  public void continueButton() {
    scheduledFuture.cancel(true);
    Stage stage = (Stage) continueButton.getScene().getWindow();
    stage.show();
    long getTime = timerService.getTime();
    timerService = new TimerService();

    if (CountdownTimerConfiguration.isCountdownTimer())
      timerService.setCountdownProcess(process);
    else timerService.setFxProcess(process);
    timerService.setTime(getTime);
    timerService.start();
    continueButton.setVisible(false);
    pauseButton.setVisible(true);
    isPause = false;
    isContinue = false;
    checkIfContinue();
    checkIfPause();
  }

  private void checkIfContinue() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable command = () -> Platform.runLater(() -> {

      if (isContinue) {
        continueButton();
      }
    });

    scheduledFuture = executor.scheduleAtFixedRate(command, 0, 1, TimeUnit.SECONDS);
  }

  private void checkIfPause() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable command = () -> Platform.runLater(() -> {

      if (isPause) {
        timerService.reRun();
        Stage stages = (Stage) continueButton.getScene().getWindow();
        stages.hide();
      }
    });

    scheduledFuture = executor.scheduleAtFixedRate(command, 0, 1, TimeUnit.SECONDS);
  }

  private void notification(String notif) {
    Notifications notifications = Notifications.create()
        .title("TRIBELY")
        .text("You only have " + notif)
        .position(Pos.BOTTOM_RIGHT)
        .hideAfter(Duration.seconds(5));
    notifications.darkStyle();
    notifications.showWarning();
  }
}
