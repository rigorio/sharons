package inc.pabacus.TaskMetrics.desktop.timesheet;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.hardware.WindowsHardwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLogFXAdapter;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogStatus;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.hardware.HardwareView;
import inc.pabacus.TaskMetrics.desktop.software.SoftwareView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
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

  private static final int DEF_SIZE = 900;

  private DailyLogService dailyLogHandler;
  private MockUser mockUser;
  private UserHandler userHandler;

  public TimesheetPresenter() {
    dailyLogHandler = BeanManager.dailyLogService();
    userHandler = BeanManager.userHandler();
  }

  @Override
  @SuppressWarnings("all")
  public void initialize(URL location, ResourceBundle resources) {
    makeFadeIn();
    mockUser = new MockUser("Rigo", "Logged Out");
    statusText.setText(mockUser.getStatus());
    userName.setText(userHandler.getUsername()); //set username
    initOshiInfo();
    initTimeSheet();
    populateCombobox();
  }

  @FXML
  public String changeStatus() {
    String currentStatus = mockUser.getStatus();
    switch (currentStatus) {
      case "Logged Out":
        currentStatus = "Logged In";
        dailyLogHandler.changeLog(LogStatus.IN.getStatus());
        break;
      case "Logged In":
        currentStatus = "Out To Lunch";
        dailyLogHandler.changeLog(LogStatus.OTL.getStatus());
        break;
      case "Out To Lunch":
        currentStatus = "Back From Lunch";
        dailyLogHandler.changeLog(LogStatus.BFL.getStatus());
        break;
      case "Back From Lunch":
        currentStatus = "Logged Out";
        dailyLogHandler.changeLog(LogStatus.OUT.getStatus());
        break;
    }
    mockUser.setStatus(currentStatus);
    statusText.setText(currentStatus);
    refreshTimesheetTable();
    return currentStatus;
  }

  private void populateCombobox() {
    ObservableList<String> choices = FXCollections.observableArrayList();
    choices.add("Morning Break");
    choices.add("Afternoon Break");
    choices.add("Meeting");
    choices.add("Training"); // TODO turn off activity listening dailyLogHandler when on a break
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

    TableColumn<DailyLogFXAdapter, String> otl = new TableColumn<>("OTL");
    otl.setCellValueFactory(param -> param.getValue().getOtl());

    TableColumn<DailyLogFXAdapter, String> bfl = new TableColumn<>("BFL");
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

  private void makeFadeIn() {
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(1000)); // 1 second
    fadeTransition.setNode(mainPane);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.play();
  }
}
