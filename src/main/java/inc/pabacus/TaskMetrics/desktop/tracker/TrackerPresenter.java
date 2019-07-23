package inc.pabacus.TaskMetrics.desktop.tracker;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.Record;
import inc.pabacus.TaskMetrics.api.activity.RecordType;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskPostEntity;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.desktop.breakTimer.BreakView;
import inc.pabacus.TaskMetrics.desktop.settings.ExtendConfiguration;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.TimerService;
import inc.pabacus.TaskMetrics.utils.XpmHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

  @FXML
  private ContextMenu contextMenu;

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
    checkIfPauseOrContinue();
    windowIsOpen = true;
    isPause = false;

    addCompleteContextMenu();
    addPauseContextMenu();

  }

  private void addPauseContextMenu() {
    ContextMenu contextMenu = new ContextMenu();

    MenuItem breaks = new MenuItem("Breaks");
    MenuItem lunch = new MenuItem("Lunch");
    MenuItem willWorkOnDifferentTask = new MenuItem("Will work on different task");
    MenuItem testingAFeature = new MenuItem("Testing a feature");
    MenuItem developmentCauses = new MenuItem("Development Causes");
    MenuItem meeting = new MenuItem("Meeting");

    breaks.setOnAction(event -> {
      isPause = true;
      Activity activity;
      activity = Activity.BREAK;
      activityHandler.saveTimestamp(activity);
      timerService.reRun(); // rerun services
      GuiManager.getInstance().displayView(new BreakView());
      notification("Status changed to Break");
      title.getScene().getWindow().hide();
    });

    lunch.setOnAction(event -> {
      isPause = true;
      Activity activity;
      activity = Activity.LUNCH;
      activityHandler.saveTimestamp(activity);
      dailyLogHandler.changeLog(LogStatus.LB.getStatus());
      timerService.reRun(); // rerun services
      GuiManager.getInstance().displayView(new BreakView());
      notification("Status changed to Lunch");
      title.getScene().getWindow().hide();
    });

    willWorkOnDifferentTask.setOnAction(event -> {
      Activity activity;
      activity = Activity.BUSY;
      activityHandler.saveTimestamp(activity);
      updateTask(Status.IN_PROGRESS.getStatus());
      saveAndClose();
    });

    testingAFeature.setOnAction(event -> {
      Activity activity;
      activity = Activity.BUSY;
      activityHandler.saveTimestamp(activity);
      timeCompensation = 0.25;
      updateTask(Status.IN_PROGRESS.getStatus());
      saveAndClose();
    });

    developmentCauses.setOnAction(event -> {
      Activity activity;
      activity = Activity.BUSY;
      activityHandler.saveTimestamp(activity);
      timeCompensation = 0.5;
      updateTask(Status.IN_PROGRESS.getStatus());
      saveAndClose();
    });

    meeting.setOnAction(event -> {
      Activity activity;
      activity = Activity.BUSY;
      activityHandler.saveTimestamp(activity);
      updateTask(Status.IN_PROGRESS.getStatus());
      saveAndClose();
    });

    contextMenu.getItems().addAll(breaks, lunch, willWorkOnDifferentTask, testingAFeature, developmentCauses, meeting);

    pauseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        contextMenu.show(pauseButton, event.getScreenX(), event.getScreenY());
      }
    });

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

  private void addCompleteContextMenu() {

    ContextMenu contextMenu = new ContextMenu();

    MenuItem ten = new MenuItem("10%");
    MenuItem thirty = new MenuItem("30%");
    MenuItem fifty = new MenuItem("50%");
    MenuItem seventy = new MenuItem("70%");
    MenuItem eighty = new MenuItem("80%");
    MenuItem ninety = new MenuItem("90%");
    MenuItem onehundred = new MenuItem("100%");

    ten.setOnAction(event -> {
      updateTask(Status.IN_PROGRESS.getStatus());
      selectedTask.setPercentCompleted(new SimpleStringProperty(ten.getText()));
      saveAndClose();
    });

    thirty.setOnAction(event -> {
      updateTask(Status.IN_PROGRESS.getStatus());
      selectedTask.setPercentCompleted(new SimpleStringProperty(thirty.getText()));
      saveAndClose();
    });

    fifty.setOnAction(event -> {
      updateTask(Status.IN_PROGRESS.getStatus());
      selectedTask.setPercentCompleted(new SimpleStringProperty(fifty.getText()));
      saveAndClose();
    });

    seventy.setOnAction(event -> {
      updateTask(Status.IN_PROGRESS.getStatus());
      selectedTask.setPercentCompleted(new SimpleStringProperty(seventy.getText()));
      saveAndClose();
    });

    eighty.setOnAction(event -> {
      updateTask(Status.IN_PROGRESS.getStatus());
      selectedTask.setPercentCompleted(new SimpleStringProperty(eighty.getText()));
      saveAndClose();
    });

    ninety.setOnAction(event -> {
      updateTask(Status.IN_PROGRESS.getStatus());
      selectedTask.setPercentCompleted(new SimpleStringProperty(ninety.getText()));
      saveAndClose();
    });

    onehundred.setOnAction(event -> {
      selectedTask.setEndTime(new SimpleStringProperty(getCurrentTime()));
      updateTask(Status.DONE.getStatus());
      selectedTask.setPercentCompleted(new SimpleStringProperty(onehundred.getText()));
      selectedTask.setDateFinished(new SimpleStringProperty(LocalTime.now().toString()));
      saveAndClose();
    });

    contextMenu.getItems().addAll(ten, thirty, fifty, seventy, eighty, ninety, onehundred);

    complete.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        contextMenu.show(complete, event.getScreenX(), event.getScreenY());
      }
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
    XpmTaskPostEntity helpMe = new XpmHelper().helpMe(xpmTask);
    xpmTaskWebHandler.edit(helpMe);
//    xpmTaskWebHandler.save(xpmTask);
    activityHandler.saveRecord(Record.builder()
                                   .recordType(RecordType.TASK)
                                   .duration("" + roundOffDecimal(getRawComputedTime()))
                                   .activity(xpmTask.getTask())
                                   .build());
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
          notification("You only have 10 minutes");
        extend.setVisible(true);
        cancel.setVisible(false);
        tenMinutes += 1;
        timer.setStyle("-fx-text-fill: yellow");
      } else if (duration <= 120 && duration >= 1) { //2 minutes in seconds
        timer.setText(time);
        if (twoMinutes == 1)
          notification("You only have 2 minutes");
        extend.setVisible(true);
        cancel.setVisible(false);
        twoMinutes += 1;
        timer.setStyle("-fx-text-fill: red");
      } else if (duration == 0) { //0 timer will stop and then
        Thread.currentThread().interrupt();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Timer Stopped");
        alert.setContentText("Time's up! Will now complete the task.");
        alert.showAndWait();
        timer.setText(STARTING_TIME);

        //complete the task
        selectedTask.setEndTime(new SimpleStringProperty(getCurrentTime()));
        updateTask(Status.DONE.getStatus());
        selectedTask.setPercentCompleted(new SimpleStringProperty("100%"));
        saveAndClose();
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
    selectedTask = null;
    closeCheckIfPauseOrContinue();
    timerService.pause();
    windowIsOpen = false;
    isPause = false;
    isContinue = false;
    ((Stage) title.getScene().getWindow()).close();
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

  @FXML
  public void continueButton() {
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
  }

  private void checkIfPauseOrContinue() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable command = () -> Platform.runLater(() -> {

      if (isPause) {
        isPause = false;
        timerService.reRun();
        title.getScene().getWindow().hide();
      } else if (isContinue) {
        continueButton();
      }
    });
    scheduledFuture = executor.scheduleAtFixedRate(command, 0, 1, TimeUnit.SECONDS);
  }

  private void closeCheckIfPauseOrContinue() {
    scheduledFuture.cancel(true);
  }

  private void notification(String notif) {
    Notifications notifications = Notifications.create()
        .title("TRIBELY")
        .text(notif)
        .position(Pos.BOTTOM_RIGHT)
        .hideAfter(Duration.seconds(5));
    notifications.darkStyle();
    notifications.showWarning();
  }
}
