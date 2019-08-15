package inc.pabacus.TaskMetrics.desktop.settings;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import inc.pabacus.TaskMetrics.api.generateToken.AuthenticatorService;
import inc.pabacus.TaskMetrics.api.generateToken.ReqEnt;
import inc.pabacus.TaskMetrics.api.hardware.HardwareData;
import inc.pabacus.TaskMetrics.api.hardware.HardwareDataFXAdapter;
import inc.pabacus.TaskMetrics.api.hardware.HardwareService;
import inc.pabacus.TaskMetrics.api.hardware.WindowsHardwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareData;
import inc.pabacus.TaskMetrics.api.software.SoftwareDataFXAdapter;
import inc.pabacus.TaskMetrics.api.software.SoftwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareService;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Task;
import inc.pabacus.TaskMetrics.desktop.newTask.DefaultTaskHolder;
import inc.pabacus.TaskMetrics.desktop.tracker.AlwaysOnTopCheckerConfiguration;
import inc.pabacus.TaskMetrics.desktop.tracker.CountdownTimerConfiguration;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import lombok.Data;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class SettingsPresenter implements Initializable {

  @FXML
  private JFXTextField hureyHostTextBox;
  @FXML
  private JFXTextField hostTextBox;
  @FXML
  private JFXComboBox<String> jobBox;
  @FXML
  private JFXComboBox<String> taskBox;
  @FXML
  private JFXTreeTableView<SoftwareDataFXAdapter> softwareTable;
  @FXML
  private JFXTreeTableView<HardwareDataFXAdapter> hardwareTable;
  @FXML
  private TableView managerTable;
  @FXML
  private JFXComboBox<String> managerBox;
  @FXML
  private JFXCheckBox alwaysOnTopCheckbox;
  @FXML
  private JFXCheckBox countdownTimer;
  @FXML
  private JFXTextField extendField;

  @FXML
  private JFXButton extendButton;

  private AuthenticatorService authenticatorService;
  private JobTaskHandler jobTaskHandler;
  private HostConfig hostConfig;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hostConfig = new HostConfig();
    hostTextBox.setText(hostConfig.getHost());
    hureyHostTextBox.setText(hostConfig.getHrisHost());
    authenticatorService = new AuthenticatorService();

    jobTaskHandler = new JobTaskHandler();
    if (DefaultTaskHolder.getDefaultJob() == null)
      jobBox.setPromptText("Select a default Job");
    else
      jobBox.setValue(DefaultTaskHolder.getDefaultJob());
    if (DefaultTaskHolder.getDefaultTask() == null)
      taskBox.setPromptText("Select a default Task");
    else
      taskBox.setValue(DefaultTaskHolder.getDefaultTask());

    jobBox.setItems(FXCollections.observableArrayList(getJobs()));

    alwaysOnTop();
    countdownTimer();
    getDefaultExtend();
    managerBox.getItems().addAll(populateManagers());
    TableColumn<String, String> managers = new TableColumn("Managers");
    managers.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
    managerTable.getColumns().addAll(managers);
    Platform.runLater(() -> {
      initSoftware();
      initHardware();
    });
  }

  @FXML
  public void changeJob() {
    String project = jobBox.getValue();
    Optional<Job> any = jobTaskHandler.allJobs(true).stream()
        .filter(job -> job.getJob().equals(project))
        .findAny();
    if (!any.isPresent())
      System.out.println("log this");
    Job job = any.get();
    List<Task> tasks = jobTaskHandler.allTasks();
    List<String> filteredTasks = tasks.stream()
        .filter(task -> task.getJobId().equals(job.getId()))
        .map(Task::getTask)
        .collect(Collectors.toList());
    taskBox.setItems(FXCollections.observableArrayList(filteredTasks));
    taskBox.getItems().add("None");
    if (project.equals("None"))
      DefaultTaskHolder.setDefaultJob(null);
    else
      DefaultTaskHolder.setDefaultJob(project);
  }

  @FXML
  public void changeTask() {
    String task = taskBox.getValue();
    if (task.equals("None"))
      DefaultTaskHolder.setDefaultTask(null);
    else
      DefaultTaskHolder.setDefaultTask(task);
  }

  private List<String> getJobs() {
    return jobTaskHandler.allJobs(true).stream()
        .map(Job::getJob)
        .collect(Collectors.toList());
  }

  private void initHardware() {
    HardwareService hardwareService = new WindowsHardwareHandler();
    ObservableList<HardwareDataFXAdapter> hardwares = FXCollections.observableArrayList();

    JFXTreeTableColumn<HardwareDataFXAdapter, String> name = new JFXTreeTableColumn<>("Hardware");
    name.setCellValueFactory(param -> param.getValue().getValue().getName());

    JFXTreeTableColumn<HardwareDataFXAdapter, String> description = new JFXTreeTableColumn<>("Name");
    description.setCellValueFactory(param -> param.getValue().getValue().getDescription());

    name.prefWidthProperty().bind(hardwareTable.widthProperty().divide(2));
    description.prefWidthProperty().bind(hardwareTable.widthProperty().divide(2));

    List<HardwareData> disks = hardwareService.getDisks();
    List<HardwareData> displays = hardwareService.getDisplays();
    List<HardwareData> usbDevices = hardwareService.getUsbDevices();
    disks.forEach(disk -> hardwares.add(new HardwareDataFXAdapter(disk)));
    displays.forEach(display -> hardwares.add(new HardwareDataFXAdapter(display)));
    usbDevices.forEach(usbDevice -> hardwares.add(new HardwareDataFXAdapter(usbDevice)));

    final TreeItem<HardwareDataFXAdapter> root = new RecursiveTreeItem<>(hardwares, RecursiveTreeObject::getChildren);
    hardwareTable.getColumns().addAll(name, description);
    hardwareTable.setRoot(root);
    hardwareTable.setShowRoot(false);

  }

  private void initSoftware() {
    SoftwareService softwareService = new SoftwareHandler();
    ObservableList<SoftwareDataFXAdapter> softwares = FXCollections.observableArrayList();

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> name = new JFXTreeTableColumn<>("Software");
    name.setCellValueFactory(param -> param.getValue().getValue().getName());

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> version = new JFXTreeTableColumn<>("Version");
    version.setCellValueFactory(param -> param.getValue().getValue().getVersion());

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> installedDate = new JFXTreeTableColumn<>("Last Updated");
    installedDate.setCellValueFactory(param -> param.getValue().getValue().getDateInstalled());

    name.prefWidthProperty().bind(softwareTable.widthProperty().divide(2));
    version.prefWidthProperty().bind(softwareTable.widthProperty().divide(4));
    installedDate.prefWidthProperty().bind(softwareTable.widthProperty().divide(4));

    List<SoftwareData> allSoftware = softwareService.getSoftware();

    for (SoftwareData s : allSoftware)
      softwares.add(new SoftwareDataFXAdapter(s));

    final TreeItem<SoftwareDataFXAdapter> root = new RecursiveTreeItem<>(softwares, RecursiveTreeObject::getChildren);
    softwareTable.getColumns().addAll(name, version, installedDate);
    softwareTable.setRoot(root);
    softwareTable.setShowRoot(false);
  }

  private void getDefaultExtend() {
    try {
      if (ExtendConfiguration.getExtendMinutes().isEmpty()) {
        ExtendConfiguration.setExtendMinutes("15");
        extendField.setText("15");
      } else
        extendField.setText(ExtendConfiguration.getExtendMinutes());
    } catch (Exception e) {
      //to make sure set the extend minutes
      extendField.setText("15");
      ExtendConfiguration.setExtendMinutes("15");
    }
  }

  @FXML
  private void extendButton() {
    if (!isEmpty())
      ExtendConfiguration.setExtendMinutes(extendField.getText());
    else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Empty");
      alert.setContentText("Please do not leave the textfield empty.");
      alert.showAndWait();
      extendField.setText("15");
    }
  }

  @FXML
  public void addManager() {
    String manager = managerBox.getValue();
//    managerTable.setItems();
  }

  @FXML
  public void removeManager() {
  }

  @FXML
  public void updateHost() {
    hostConfig.updateHost(hostTextBox.getText());
  }

  @FXML
  public void connectHureyHost() {
    StringCacheService cacheService = new StringCacheService();
    hostConfig.updateHureyHost(hureyHostTextBox.getText());
    cacheService.put(CacheKey.HUREY_HOST, hureyHostTextBox.getText());
    Pair<String, String> tribelyLogin = showTribelyLogin();
    String username = tribelyLogin.getKey();
    String password = tribelyLogin.getValue();
    ReqEnt reqEnt = authenticatorService.retrieveHureyItems(username, password);
    if (!reqEnt.isSuccessful()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Unsuccessful login");
      alert.setContentText(reqEnt.getError());
      alert.showAndWait();
      return;
    }
    cacheService.put(CacheKey.EMPLOYEE_ID, reqEnt.getEmployeeId());
    cacheService.put(CacheKey.SHRIS_TOKEN, reqEnt.getHureyToken());
    authenticatorService.retrieveEmployeeManagerId();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logged In!");
    alert.setContentText("Success!");
    alert.showAndWait();
  }

  public ObservableList<String> populateManagers() {
    List<String> names = new ArrayList<>();
    names.add("Joy Cuison");
    names.add("Mae Cayabyab");
    names.add("Rigo Sarmiento");
    names.add("Carlo Montemayor");
    names.add("Chris Nebril");
    names.add("Gabrielle Floresca");
    names.add("Edmond Balingit");
    return FXCollections.observableArrayList(names);
  }

  private void alwaysOnTop() {
    if (AlwaysOnTopCheckerConfiguration.isAlwaysOnTop())
      alwaysOnTopCheckbox.setSelected(true);
    else
      alwaysOnTopCheckbox.setSelected(false);

    alwaysOnTopCheckbox.selectedProperty()
        .addListener((observable, oldValue, newValue) -> AlwaysOnTopCheckerConfiguration.setAlwaysOnTop(newValue));
  }

  private void countdownTimer() {
    if (CountdownTimerConfiguration.isCountdownTimer())
      countdownTimer.setSelected(true);
    else
      countdownTimer.setSelected(false);

    countdownTimer.selectedProperty()
        .addListener((observable, oldValue, newValue) -> CountdownTimerConfiguration.setCountdownTimer(newValue));
  }

  private boolean isEmpty() {
    return extendField.getText().isEmpty();
  }

  @Data
  private class Person {
    StringProperty name;
  }


  private Pair<String, String> showTribelyLogin() {
    // Create the custom dialog.
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    dialog.setTitle("Login");
    dialog.setHeaderText("Please Login with your Tribely Account");

// Set the button types.
    ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField username = new TextField();
    username.setPromptText("Username");
    PasswordField password = new PasswordField();
    password.setPromptText("Password");

    grid.add(new Label("Username:"), 0, 0);
    grid.add(username, 1, 0);
    grid.add(new Label("Password:"), 0, 1);
    grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
    Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
    loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
    username.textProperty().addListener((observable, oldValue, newValue) -> {
      loginButton.setDisable(newValue.trim().isEmpty());
    });

    dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
    Platform.runLater(username::requestFocus);

// Convert the result to a username-password-pair when the login button is clicked.
    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == loginButtonType) {
        return new Pair<>(username.getText(), password.getText());
      }
      return null;
    });

    Optional<Pair<String, String>> result = dialog.showAndWait();
    return result.get();
  }
}
