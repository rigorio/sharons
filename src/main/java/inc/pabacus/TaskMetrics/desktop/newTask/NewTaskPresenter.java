package inc.pabacus.TaskMetrics.desktop.newTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.generateToken.UsernameHolder;
import inc.pabacus.TaskMetrics.api.tasks.*;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValue;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValueHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Task;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.utils.XpmHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NewTaskPresenter implements Initializable {

  @FXML
  private Label customTaskLabel;
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
  private Label estimateLabel;

  @FXML
  private JFXTextField estimateField;

  private BusinessValueHandler businessValueHandler = new BusinessValueHandler();
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
    jobComboBox.setPromptText("Select a job");
    if (DefaultTaskHolder.getDefaultJob() != null) {
      jobComboBox.setValue(DefaultTaskHolder.getDefaultJob());
      changeTask(DefaultTaskHolder.getDefaultJob());
    }
    taskCombobox.setPromptText("Select a task");
    if (DefaultTaskHolder.getDefaultTask() != null)
      taskCombobox.setValue(DefaultTaskHolder.getDefaultTask());
    jobComboBox.setItems(FXCollections.observableArrayList(getJobs()));
    customTaskField.setVisible(false);
    customTaskLabel.setVisible(false);
    descriptionLabel.setLayoutY(205);
    descriptionField.setLayoutY(201);
    estimateLabel.setLayoutY(170);
    estimateField.setLayoutY(165);

    estimateFieldTextProperty();
  }

  private void estimateFieldTextProperty() {
    Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
    TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
      return pattern.matcher(change.getControlNewText()).matches() ? change : null;
    });

    estimateField.setTextFormatter(formatter);
  }

  private List<String> getJobs() {
    return jobTaskHandler.allJobs().stream()
        .map(Job::getJob)
        .collect(Collectors.toList());
  }

  @FXML
  public void changeTasks() {
    String project = jobComboBox.getValue();
    changeTask(project);
  }

  @SuppressWarnings("all")
  private void changeTask(String project) {
    Job job = getSelectedJob(project);
    List<Task> tasks = getTasks();
    List<String> filteredTasks = tasks.stream()
        .filter(task -> task.getJobId().equals(job.getId()))
        .map(Task::getTask)
        .collect(Collectors.toList());
    taskCombobox.setItems(FXCollections.observableArrayList(filteredTasks));
    taskCombobox.getItems().add("Custom Task");
  }

  private List<Task> getTasks() {
    return jobTaskHandler.allTasks();
  }

  @NotNull
  private Job getSelectedJob(String project) {
    Optional<Job> any = jobTaskHandler.allJobs().stream()
        .filter(job -> job.getJob().equals(project))
        .findAny();
    if (!any.isPresent())
      System.out.println("new task presenter line 137");
    return any.get();
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
    String estimateFields = estimateField.getText();
    String taskTitle = taskCombobox.getValue();


    Job job = getSelectedJob(jobComboBox.getValue());
    Optional<Task> any = getTasks().stream()
        .filter(task -> {
          return task.getTask().equalsIgnoreCase(taskTitle) &&
              task.getJobId().equals(job.getId());
        })
        .findAny();

    if (!any.isPresent()) {
      System.out.println("new task presenter line 180");
    }

    Task task = any.get();

//    BusinessValue businessValue = getBusinessValue();
//    Project project = getProject();

    XpmTask xpmTask = XpmTask.builder()
        .id(0L)
        .task(taskTitle)
        .job(jobComboBox.getValue())
        .billable(true)
        .estimateTime(estimateFields)
        .invoiceTypeId(new InvoiceType(1L, "Staff"))
        .assigneeId(new Assignee(1L, UsernameHolder.username))
        .totalTimeSpent("0.0")
        .businessValueId(1L)
        .status(Status.PENDING.getStatus())
        .description(description)
        .dateCreated(LocalDate.now().toString())
        .build();
    XpmTaskPostEntity xpmTaskPostEntity = new XpmHelper().helpMe(xpmTask);
    xpmTaskHandler.save(xpmTaskPostEntity);

//    xpmTaskHandler.save(xpmTask);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Task saved!");
    alert.showAndWait();
    Stage stage = (Stage) jobComboBox.getScene().getWindow();
    stage.close();

  }


  private List<BusinessValue> getAllBusinessValues() {
    return businessValueHandler.getAll();
  }

  private boolean isAlrightAlrightAlright() {
    return jobComboBox.getSelectionModel().isEmpty() || taskCombobox.getSelectionModel().isEmpty() || descriptionField.getText().isEmpty() || estimateField.getText().isEmpty();
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
      descriptionLabel.setLayoutY(205);
      descriptionField.setLayoutY(201);
      estimateLabel.setLayoutY(170);
      estimateField.setLayoutY(165);
    } else {

      customTaskField.setVisible(true);
      customTaskLabel.setVisible(true);
      descriptionLabel.setLayoutY(246);
      descriptionField.setLayoutY(244);
      estimateLabel.setLayoutY(205);
      estimateField.setLayoutY(201);
    }
  }
}
