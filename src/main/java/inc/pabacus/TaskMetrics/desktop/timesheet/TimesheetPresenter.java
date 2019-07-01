package inc.pabacus.TaskMetrics.desktop.timesheet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.hardware.WindowsHardwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLogFXAdapter;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.hardware.HardwareView;
import inc.pabacus.TaskMetrics.desktop.software.SoftwareView;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
  private Label statusText;
  @FXML
  private Label userName;
  @FXML
  private ImageView softwareImg;
  @FXML
  private ImageView hardwareImg;
  @FXML
  private Label os;
  @FXML
  private Label hardware;
  @FXML
  private TableView<DailyLogFXAdapter> timeSheet;
  @FXML
  private Label dateLabel;

  private static final int DEF_SIZE = 900;

  private DailyLogService dailyLogHandler;
  private MockUser mockUser;
  private UserHandler userHandler;
  private ActivityHandler activityHandler;

  private static final String HOST = "http://localhost:8080";

  public TimesheetPresenter() {
    dailyLogHandler = BeanManager.dailyLogService();
    userHandler = BeanManager.userHandler();
    activityHandler = BeanManager.activityHandler();
  }

  @Override
  @SuppressWarnings("all")
  public void initialize(URL location, ResourceBundle resources) {

    mockUser = new MockUser("Rigo", "Logged Out");
    userName.setText(userHandler.getUsername()); //set username

    getStatus();
    initOshiInfo();
    initTimeSheet();
    populateCombobox();
  }

  @FXML
  public String changeStatus() {
    String currentStatus = statusText.getText();

    if (TrackHandler.getSelectedTask() != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not Allowed");
      alert.setHeaderText(null);
      alert.setContentText("Cannot change activity while tracking a task");
      alert.showAndWait();
      return currentStatus;
    }

    Activity activity = Activity.BUSY; // default ?

    switch (currentStatus) {
      case "Logged Out":
        currentStatus = "Logged In";
        dailyLogHandler.changeLog(LogStatus.IN.getStatus());
        activity = Activity.ONLINE;
        break;
      case "Logged In":
        currentStatus = "Lunch Break";
        dailyLogHandler.changeLog(LogStatus.LB.getStatus());
        activity = Activity.BREAK;
        break;
      case "Out To Lunch":
        currentStatus = "Back From Break";
        dailyLogHandler.changeLog(LogStatus.BFB.getStatus());
        activity = Activity.BUSY;
        break;
      case "Back From Lunch":
        currentStatus = "Logged Out";
        dailyLogHandler.changeLog(LogStatus.OUT.getStatus());
        activity = Activity.OFFLINE;
        break;
    }
    mockUser.setStatus(currentStatus);
    statusText.setText(currentStatus);
    refreshTimesheetTable();
    activityHandler.saveActivity(activity);
    return currentStatus;
  }

  @FXML
  public void updateStatus() {
    String activity = comboBox.getValue();
    activityHandler.saveActivity(activity);
  }

  private void populateCombobox() {
    ObservableList<String> choices = FXCollections.observableArrayList();
    choices.add("Break");
    choices.add("Meeting"); // TODO turn off activity listening dailyLogHandler when on a break
    comboBox.getItems().addAll(choices);
//    comboBox = new JFXComboBox<>(choices);
  }

  @FXML
  public void viewHardware() {
    GuiManager.getInstance().displayView(new HardwareView());
  }

  @FXML
  public void viewSoftware() {
    GuiManager.getInstance().displayView(new SoftwareView());
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
    getLogs();
    timeSheet.getColumns().addAll(date, in, otl, bfl, out);

    refreshTimesheetTable();

  }

  private ObservableList<DailyLogFXAdapter> getLogs() {
    List<DailyLogFXAdapter> logs = dailyLogHandler.getAllLogs().stream()
        .map(DailyLogFXAdapter::new)
        .collect(Collectors.toList());
    return FXCollections.observableArrayList(logs);
  }

  private void refreshTimesheetTable() {
    timeSheet.setItems(getLogs());
  }

  private void initOshiInfo() {
    softwareImg.setImage(new Image("/img/software.png", DEF_SIZE, DEF_SIZE, false, true));
    hardwareImg.setImage(new Image("/img/hardware.png", DEF_SIZE, DEF_SIZE, false, true));

    os.setText(new SoftwareHandler().getOs());
    hardware.setText(new WindowsHardwareHandler().getAllInfo().getProcessor().getName());
  }
  // TODO refactor/extract. Does not follow code by responsibility

  private void getStatus() {
//    String in = null, otl = null, bfl = null, out = null;
    LocalDate dateNow = LocalDate.now();

    OkHttpClient client = new OkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/logs")
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", TokenRepository.getToken().getToken())
        .method("GET", null)
        .build();

    try {
      Response response = client.newCall(request).execute();
      String jsonString = response.body().string();

      List<DailyLog> dailyLogs = new ObjectMapper().readValue(jsonString, new TypeReference<List<DailyLog>>() {});
      Optional<DailyLog> any = dailyLogs.stream()
          .filter(dailyLog -> dailyLog.getDate().equals(dateNow.toString()))
          .findAny();
      String status;
      if (!any.isPresent())
        status = "Logged Out";
      else {
        DailyLog dailyLog = any.get();
        String in = dailyLog.getIn();
        String otl = dailyLog.getOtl();
        String bfl = dailyLog.getBfl();
        String out = dailyLog.getOut();
        if (in == null || in.equals("null"))
          status = "Logged Out";
        else if (otl == null || otl.equals("null"))
          status = "Logged In";
        else if (bfl == null || bfl.equals("null"))
          status = "Lunch Break";
        else if (out == null || out.equals("null"))
          status = "Back From Break";
        else
          status = "Logged Out";
      }
      statusText.setText(status);
/*
      JSONArray jsonarray = new JSONArray(jsonString);
      for (int i = 0; i < jsonarray.length(); i++) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
        dateLabel.setText(jsonobject.getString("date"));
        if (dateLabel.getText().equalsIgnoreCase(String.valueOf(dateNow))) {
          in = jsonobject.getString("in");
          otl = jsonobject.getString("otl");
          bfl = jsonobject.getString("bfl");
          out = jsonobject.getString("out");
        }
      }
      */
    } catch (JSONException | IOException e) {
      System.out.println(e); // TODO log exception
    }
/*
    if (dateLabel.getText().equalsIgnoreCase(String.valueOf(dateNow))) {
      if (in == null || in.equals("null")) {
        statusText.setText("Logged Out");
      } else if (otl == null || otl.equals("null")) {
        statusText.setText("Logged In");
      } else if (bfl == null || bfl.equals("null")) {
        statusText.setText("Out To Lunch");
      } else if (out == null || out.equals("null")) {
        statusText.setText("Back From Lunch");
      } else {
        statusText.setText("Logged Out"); //incase all status are null
      }
    } else {
      statusText.setText("Logged Out");
    }
    */

  }
}
