package inc.pabacus.TaskMetrics.desktop.timesheet;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.timesheet.handlers.HRISLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.handlers.LogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLogFXAdapter;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.breakTimer.BreakPresenter;
import inc.pabacus.TaskMetrics.desktop.breakTimer.BreakView;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import okhttp3.OkHttpClient;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TimesheetPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private JFXComboBox<String> comboBox;
  @FXML
  private JFXButton updateButton;
  @FXML
  private JFXButton statusButton;
  @FXML
  private Label userName;
  @FXML
  private TableView<DailyLogFXAdapter> timeSheet;
  @FXML
  private Label dateLabel;

  private static final int DEF_SIZE = 900;

  private MockUser mockUser;
  private UserHandler userHandler;
  private ActivityHandler activityHandler;
  private HostConfig hostConfig = new HostConfig();
  private LogService logService;
  private OkHttpClient client = SslUtil.getSslOkHttpClient();

  private static String HOST;

  public TimesheetPresenter() {
    HOST = hostConfig.getHost();
    userHandler = BeanManager.userHandler();
    activityHandler = BeanManager.activityHandler();
    logService = new HRISLogHandler();
  }

  @Override
  @SuppressWarnings("all")
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(() -> {
      mockUser = new MockUser("Rigo", "Logged Out");
      userName.setText(userHandler.getUsername()); //set userName

      String status = getStatus();
      comboBox.setPromptText(status);
      initTimeSheet();
      populateCombobox();
    });
  }

  @FXML
  public void updateStatus() {
    String chosenStatus = comboBox.getValue();
    if (chosenStatus.equals("Break")) {
      System.out.println("Spawn countdown timer");
    } else if (chosenStatus.equals("Meeting")) {
      System.out.println("Spawn MEETING DONE box");
    } else {

      if (TrackHandler.getSelectedTask() != null) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not Allowed");
        alert.setHeaderText(null);
        alert.setContentText("Cannot change activity while tracking a task");
        alert.showAndWait();
        return;
      }

      Activity activity = Activity.BUSY; // default ?
      LogStatus status = null;
      switch (chosenStatus) {
        case "Time Out":
          status = LogStatus.OUT;
          activity = Activity.OFFLINE;
          break;
        case "Time In":
          status = LogStatus.IN;
          activity = Activity.ONLINE;
          break;
        case "Lunch Break":
          if (BreakPresenter.windowIsOpen) {
            notification("Break Timer is currently on!");
            break;
          } else {
            status = LogStatus.LB;
            activity = Activity.LUNCH;
            GuiManager.getInstance().displayView(new BreakView());
            notification("Status changed to Break");
            break;
          }
        case "Back From Break":
          if (BreakPresenter.windowIsOpen) {
            notification("Break Timer is currently on, please Back Online!");
            break;
          } else {
            status = LogStatus.BFB;
            activity = Activity.BUSY;
            break;
          }
      }
      logService.changeLog(status.getStatus());
      mockUser.setStatus(chosenStatus);
      comboBox.setValue(chosenStatus);
      refreshTimesheetTable();
      activityHandler.saveTimestamp(activity);
    }
  }

  private void populateCombobox() {

    List<String> statuses = new ArrayList<>();
    statuses.add("Time In");
    statuses.add("Lunch Break");
    statuses.add("Back From Break");
    statuses.add("Time Out");
    statuses.add("Break");
    statuses.add("Meeting");
    ObservableList<String> defaultChoices = FXCollections.observableArrayList(statuses);
    comboBox.getItems().addAll(defaultChoices);

  }

  private void initTimeSheet() {

    TableColumn<DailyLogFXAdapter, String> date = new TableColumn<>("DATE");
    date.setCellValueFactory(param -> param.getValue().getDate());

    TableColumn<DailyLogFXAdapter, String> in = new TableColumn<>("IN");
    in.setCellValueFactory(param -> param.getValue().getIn());

    TableColumn<DailyLogFXAdapter, String> otl = new TableColumn<>("LB");
    otl.setCellValueFactory(param -> param.getValue().getOtl());

    TableColumn<DailyLogFXAdapter, String> bfl = new TableColumn<>("BFB");
    bfl.setCellValueFactory(param -> param.getValue().getBfl());

    TableColumn<DailyLogFXAdapter, String> out = new TableColumn<>("OUT");
    out.setCellValueFactory(param -> param.getValue().getOut());

//    timeSheet.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

    //1 = 1 whole, 2 = 1/2, 8 = 1/8, 4 = 1/4
    date.prefWidthProperty().bind(timeSheet.widthProperty().divide(2));
    in.prefWidthProperty().bind(timeSheet.widthProperty().divide(8));
    otl.prefWidthProperty().bind(timeSheet.widthProperty().divide(8));
    bfl.prefWidthProperty().bind(timeSheet.widthProperty().divide(8));
    out.prefWidthProperty().bind(timeSheet.widthProperty().divide(8));
//    getLogs();
    timeSheet.getColumns().addAll(date, in, otl, bfl, out);

    refreshTimesheetTable();

  }

  private List<DailyLog> allHrisTimeLogs() {
    return logService.allConvertedDailyLogs();
  }

  private ObservableList<DailyLogFXAdapter> getLogs() {
    List<DailyLogFXAdapter> logs = allHrisTimeLogs().stream()
        .map(DailyLogFXAdapter::new)
        .collect(Collectors.toList());
    return FXCollections.observableArrayList(logs);
  }

  private void refreshTimesheetTable() {
    timeSheet.setItems(getLogs());
  }

  // TODO refactor/extract. Does not follow code by responsibility

  private String getStatus() {
    return logService.getLatest();
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
