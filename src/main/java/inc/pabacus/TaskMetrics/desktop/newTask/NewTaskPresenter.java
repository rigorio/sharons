package inc.pabacus.TaskMetrics.desktop.newTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.project.ProjectHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValue;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValueHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Task;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import okhttp3.MediaType;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NewTaskPresenter implements Initializable {

  @FXML
  private Label customTaskLabel;
  @FXML
  private Label businessLabel;
  @FXML
  private Label descriptionLabel;
  @FXML
  private JFXTextField customTaskField;
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
  private XpmTaskWebHandler xpmTaskHandler = new XpmTaskWebHandler();
  private JobTaskHandler jobTaskHandler;

  public NewTaskPresenter() {
    jobTaskHandler = new JobTaskHandler();
  }

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
    jobComboBox.setItems(FXCollections.observableArrayList(getJobs()));
    customTaskField.setVisible(false);
    customTaskLabel.setVisible(false);
    businessLabel.setLayoutY(168);
    businessComboBox.setLayoutY(168);
    descriptionLabel.setLayoutY(203);
    descriptionField.setLayoutY(203);

    businessComboBox.setValue("Development");
  }

  private List<String> getJobs() {
    return jobTaskHandler.allJobs().stream()
        .map(Job::getJob)
        .collect(Collectors.toList());
  }

  @FXML
  public void changeTasks() {
    String project = jobComboBox.getValue();
    Optional<Job> any = jobTaskHandler.allJobs().stream()
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
    taskCombobox.setItems(FXCollections.observableArrayList(filteredTasks));
    taskCombobox.getItems().add("Custom Task");
  }

  @FXML
  public void selectTask() {
    String task = taskCombobox.getValue();
    boolean status = !task.equalsIgnoreCase("Custom Task");
    mutateFields(status);
  }

  @FXML
  public void close() {
    Stage stage = (Stage) jobComboBox.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void save() {

    if (isAlrightAlrightAlright() || isCustomTaskEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Error");
      alert.setContentText("Please fill out all the fields");
      alert.showAndWait();
      return;
    }

    String description = descriptionField.getText(); // actually task of task
    Boolean billable = Boolean.valueOf(taskCombobox.getValue());
    String taskValue = taskCombobox.getValue();
    String taskTitle = taskValue.equalsIgnoreCase("Custom Task")
        ? customTaskField.getText()
        : taskValue;
    BusinessValue businessValue = getBusinessValue();
//    Project project = getProject();

    XpmTask xpmTask = XpmTask.builder()
        .id(3L)
        .task(taskTitle)
        .job(jobComboBox.getValue())
        .billable(true)
        .totalTimeSpent("0.0")
        .status(Status.PENDING.getStatus())
        .description(description)
        .dateCreated(LocalDate.now().toString())
        .build();

    xpmTaskHandler.save(xpmTask);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Task saved!");
    alert.showAndWait();
    Stage stage = (Stage) jobComboBox.getScene().getWindow();
    stage.close();

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

  private boolean isCustomTaskEmpty() {
    String task = taskCombobox.getValue();
    boolean status = task.equalsIgnoreCase("Custom Task");
    if (status) {
      return customTaskField.getText().isEmpty();
    }
    return false;
  }

  private void mutateFields(boolean status) {
    if (status) {
      customTaskField.setVisible(false);
      customTaskLabel.setVisible(false);
      businessLabel.setLayoutY(168);
      businessComboBox.setLayoutY(168);
      descriptionLabel.setLayoutY(203);
      descriptionField.setLayoutY(203);
    } else {

      customTaskField.setVisible(true);
      customTaskLabel.setVisible(true);
      businessLabel.setLayoutY(203);
      businessComboBox.setLayoutY(203);
      descriptionLabel.setLayoutY(246);
      descriptionField.setLayoutY(246);
    }
  }
}
