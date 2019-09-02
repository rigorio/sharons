package inc.pabacus.TaskMetrics.desktop.newTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.tasks.TaskConnector;
import inc.pabacus.TaskMetrics.api.tasks.dto.TaskCreationDTO;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValueHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.TaskTemplate;
import inc.pabacus.TaskMetrics.utils.WindowChecker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NewTaskPresenter implements Initializable {

  @FXML
  private JFXTextField taskTextBox;
  @FXML
  private Label customTaskLabel;
  @FXML
  private Label descriptionLabel;
  @FXML
  private JFXTextField customTaskField;
  @FXML
  private JFXTextField descriptionField;
//  @FXML
//  private JFXComboBox<String> taskCombobox;
  @FXML
  private JFXButton saveButton;
  @FXML
  private JFXButton closeButton;
  //  @FXML
//  private JFXComboBox<String> jobComboBox;
  @FXML
  private Label estimateLabel;

  @FXML
  private JFXTextField estimateField;

  private BusinessValueHandler businessValueHandler = new BusinessValueHandler();
  private TaskConnector taskConnector = new TaskConnector();
  private JobTaskHandler jobTaskHandler;
  private String job = JobHolder.getJob();

  public NewTaskPresenter() {
    jobTaskHandler = new JobTaskHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    //unfocus textfield
    Platform.runLater(() -> closeButton.requestFocus()); // what

//    jobComboBox.setPromptText("Select a job");
//    if (DefaultTaskHolder.getDefaultJob() != null) {
//      jobComboBox.setValue(DefaultTaskHolder.getDefaultJob());
//      changeTask(DefaultTaskHolder.getDefaultJob());
//    }
    taskTextBox.setPromptText("Task name");
//    if (DefaultTaskHolder.getDefaultTask() != null)
//      taskTextBox.setT(DefaultTaskHolder.getDefaultTask());
//    jobComboBox.setItems(FXCollections.observableArrayList(getJobs()));
    estimateFieldTextProperty();
    changeTask(job);
  }

  private void estimateFieldTextProperty() {
    Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
    TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
      return pattern.matcher(change.getControlNewText()).matches() ? change : null;
    });

    estimateField.setTextFormatter(formatter);
  }

  private List<String> getJobs() {
    return jobTaskHandler.allJobs(true).stream()
        .map(Job::getJob)
        .collect(Collectors.toList());
  }

  @FXML
  public void changeTasks() {
//    String project = jobComboBox.getValue();
    changeTask(job);
  }

  @SuppressWarnings("all")
  private void changeTask(String project) {
    Job job = getSelectedJob(project);
    List<TaskTemplate> taskTemplates = getTasks();
    List<String> filteredTasks = taskTemplates.stream()
        .filter(task -> task.getJobId().equals(job.getId()))
        .map(TaskTemplate::getTask)
        .collect(Collectors.toList());
//    taskCombobox.setItems(FXCollections.observableArrayList(filteredTasks));
//    taskCombobox.getItems().add("Custom TaskTemplate");
  }

  private List<TaskTemplate> getTasks() {
    return jobTaskHandler.allTasks();
  }

  @NotNull
  private Job getSelectedJob(String project) {
    Optional<Job> any = jobTaskHandler.allJobs(true).stream()
        .filter(job -> job.getJob().equals(project))
        .findAny();
    if (!any.isPresent())
      System.out.println("new task presenter line 137");
    return any.get();
  }

  @FXML
  public void selectTask() {
//    String task = taskCombobox.getValue();
//    boolean status = !task.equalsIgnoreCase("Custom Task");
  }

  @FXML
  public void close() {
    WindowChecker.setNewTaskWindowOpen(false);
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void save() {

//    if (isAlrightAlrightAlright() || isCustomTaskEmpty()) {
//      Alert alert = new Alert(Alert.AlertType.WARNING);
//      alert.setTitle("Error");
//      alert.setContentText("Please fill out all the fields");
//      alert.showAndWait();
//      return;
//    }

    String description = descriptionField.getText(); // actually task of task
//    Boolean billable = Boolean.valueOf(taskCombobox.getValue());
    String estimateFields = estimateField.getText();
    String taskTitle = taskTextBox.getText();


//    Job job = getSelectedJob(jobComboBox.getValue());
//    Optional<Task> any = getTasks().stream()
//        .filter(task -> {
//          return task.getTask().equalsIgnoreCase(taskTitle) &&
//              task.getJobId().equals(job.getId());
//        })
//        .findAny();
//
//    if (!any.isPresent()) {
//      System.out.println("new task presenter line 180");
//    }

//    Task task = any.get();

//    BusinessValue businessValue = getBusinessValue();
//    Project project = getProject();
    Optional<Job> anyJob = jobTaskHandler.allJobs(true).stream()
        .filter(job -> job.getJob().equals(this.job))
        .findAny();

    Job job = anyJob.get();


    TaskCreationDTO dto = TaskCreationDTO.builder()
        .task(taskTitle)
        .jobId(job.getId())
        .description(description)
        .estimatedTime(Double.parseDouble(estimateFields))
        .build();
    taskConnector.save(dto);

//    XpmTask xpmTask = XpmTask.builder()
//        .id(0L)
//        .task(taskTitle)
//        .job(this.job)
//        .billable("Y")
//        .estimateTime(estimateFields)
////        .invoiceTypeId(new InvoiceType(1L, "Staff"))
////        .assigneeId(new Assignee(1L, UsernameHolder.username))
//        .percentCompleted("0%")
//        .totalTimeSpent("0.0")
////        .businessValueId(1L)
//        .status(Status.PENDING.getStatus())
//        .description(description)
//        .dateCreated(LocalDate.now().toString())
//        .build();
//    XpmTaskPostEntity xpmTaskPostEntity = new XpmHelper().helpMe(xpmTask);
//    taskConnector.save(xpmTaskPostEntity);

//    taskConnector.save(xpmTask);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Task saved!");
    alert.showAndWait();
    WindowChecker.setNewTaskWindowOpen(false);
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();

  }

}
