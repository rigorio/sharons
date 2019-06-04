package inc.pabacus.TaskMetrics.desktop.newTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.project.Project;
import inc.pabacus.TaskMetrics.api.project.ProjectHandler;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.TaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValue;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValueHandler;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTask;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTaskHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import okhttp3.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NewTaskPresenter implements Initializable {

  @FXML
  private JFXTextField descriptionField;

  @FXML
  private JFXComboBox<String> taskCombobox;

  @FXML
  private JFXButton saveButton;

  @FXML
  private JFXButton closeButton;

  @FXML
  private JFXComboBox<String> jobComboBox;

  @FXML
  private JFXComboBox<String> businessComboBox;

  ObservableList<String> billableList = FXCollections.observableArrayList("True", "False");

  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static final String HOST = "http://localhost:8080";
  private BusinessValueHandler businessValueHandler = new BusinessValueHandler();
  private ProjectHandler projectHandler = new ProjectHandler();
  private TaskHandler taskHandler = new TaskHandler();
  private XpmTaskHandler xpmTaskHandler = new XpmTaskHandler();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    //unfocus textfield
    Platform.runLater(() -> closeButton.requestFocus()); // what

    List<String> businesses = getAllBusinessValues().stream()
        .map(BusinessValue::getBusiness)
        .collect(Collectors.toList());
    businessComboBox.getItems().addAll(businesses);
    taskCombobox.setPromptText("Select a task");
    jobComboBox.setPromptText("Select a job");
    businessComboBox.setPromptText("Choose Business Value");
    jobComboBox.setItems(FXCollections.observableArrayList("IT Support", "Accounting"));

    businessComboBox.setValue("Development");
  }

  @FXML
  public void changeTasks() {
    String project = jobComboBox.getValue();
    List<XpmTask> xpmTasks = xpmTaskHandler.generateMockups();
    List<String> collect = xpmTasks.stream()
        .filter(xpmTask -> xpmTask.getJob().equals(project))
        .map(XpmTask::getTitle)
        .collect(Collectors.toList());
    taskCombobox.setItems(FXCollections.observableArrayList(collect));
  }

  @FXML
  public void close() {
    Stage stage = (Stage) jobComboBox.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void save() {

    if (isAlrightAlrightAlright()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Error");
      alert.setContentText("Please fill out all the fields");
      alert.showAndWait();
      return;
    }

    String description = descriptionField.getText(); // actually title of task
    Boolean billable = Boolean.valueOf(taskCombobox.getValue());
    String taskTitle = taskCombobox.getValue();
    BusinessValue businessValue = getBusinessValue();
    Project project = getProject();

    XpmTask xpmTask = XpmTask.builder()
        .id(3L)
        .title(taskTitle)
        .job(jobComboBox.getValue())
        .description(description)
        .build();

    xpmTaskHandler.save(xpmTask);


    Task task = Task.builder()
        .title(description)
        .projectId(project.getId())
        .projectName(project.getProjectName())
        .businessValueId(businessValue.getId())
        .billable(billable)
        .timeSpent(0.0)
        .status(Status.PENDING)
        .build();

    Task t = taskHandler.createTask(task);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Task saved!");
    alert.showAndWait();
    Stage stage = (Stage) jobComboBox.getScene().getWindow();
    stage.close();

  }

  private Project getProject() {
    Optional<Project> anyProject = getAllProjects().stream()
        .filter(project -> project.getProjectName().equals(jobComboBox.getValue()))
        .findAny();

    if (!anyProject.isPresent())
      throw new RuntimeException("No Project was found");

    return anyProject.get();
  }

  private BusinessValue getBusinessValue() {
    Optional<BusinessValue> any = getAllBusinessValues().stream()
        .filter(businessValue -> businessValue.getBusiness().equals(businessComboBox.getValue()))
        .findAny();

    if (!any.isPresent())
      throw new RuntimeException("No business value found"); // throw dialog box

    return any.get();
  }

  private List<BusinessValue> getAllBusinessValues() {
    return businessValueHandler.getAll();
  }

  private boolean isAlrightAlrightAlright() {
    return jobComboBox.getSelectionModel().isEmpty() || taskCombobox.getSelectionModel().isEmpty() || descriptionField.getText().isEmpty() || businessComboBox.getSelectionModel().isEmpty();
  }

  private List<Project> getAllProjects() {
    return projectHandler.getAllProjects();
  }
}
