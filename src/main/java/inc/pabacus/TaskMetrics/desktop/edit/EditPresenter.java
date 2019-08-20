package inc.pabacus.TaskMetrics.desktop.edit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.project.ProjectHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskPostEntity;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValue;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValueHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Task;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.utils.XpmHelper;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import okhttp3.MediaType;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("All")
public class EditPresenter implements Initializable {

  @FXML
  private JFXTextField dateCreatedField;
  @FXML
  private JFXComboBox statusComboBox;
  @FXML
  private JFXTextField startTimeField;
  @FXML
  private Label customTaskLabel1;
  @FXML
  private JFXTextField endTimeField;
  @FXML
  private Label customTaskLabel2;
  @FXML
  private JFXTextField totalTimeField;
  @FXML
  private Label customTaskLabel21;
  @FXML
  private Label customTaskLabel;
  @FXML
  private Label descriptionLabel;
  @FXML
  private JFXTextField customTaskField;
  @FXML
  private JFXTextField descriptionField;
  @FXML
  private JFXTextField estimateTimeField;
  @FXML
  private JFXComboBox<String> taskCombobox;
  @FXML
  private JFXButton saveButton;
  @FXML
  private JFXButton closeButton;
  @FXML
  private JFXComboBox<String> jobComboBox;

  ObservableList<String> billableList = FXCollections.observableArrayList("True", "False");

  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static String HOST;
  private BusinessValueHandler businessValueHandler = new BusinessValueHandler();
  private ProjectHandler projectHandler = new ProjectHandler();
  private XpmTaskWebHandler xpmTaskHandler = new XpmTaskWebHandler();
  private JobTaskHandler jobTaskHandler;
  private XpmTaskAdapter taskBeingEdited;

  public EditPresenter() {
    jobTaskHandler = new JobTaskHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    taskBeingEdited = EditableTaskHolder.getTask();

    //unfocus textfield
    Platform.runLater(() -> closeButton.requestFocus()); // what


    taskCombobox.setPromptText("Select a task");
    jobComboBox.setPromptText("Select a job");
    jobComboBox.setItems(FXCollections.observableArrayList(getJobs()));
    customTaskField.setVisible(false);
    customTaskLabel.setVisible(false);
    statusComboBox.setItems(FXCollections.observableArrayList(
        Status.IN_PROGRESS.getStatus(),
        Status.PENDING.getStatus(),
        Status.DONE.getStatus()
    ));
//    descriptionLabel.setLayoutY(203);
//    descriptionField.setLayoutY(203);

    System.out.println(taskBeingEdited);
    StringProperty job = taskBeingEdited.getJob();
    StringProperty task = taskBeingEdited.getTask();
    StringProperty description = taskBeingEdited.getDescription();
    StringProperty status = taskBeingEdited.getStatus();
    StringProperty estimateTime = taskBeingEdited.getEstimateTime();
    StringProperty startTime = taskBeingEdited.getStartTime();
    StringProperty endTime = taskBeingEdited.getEndTime();
    StringProperty totalTimeSpent = taskBeingEdited.getTotalTimeSpent();
    StringProperty dateCreated = taskBeingEdited.getDateCreated();
    if (job != null)
      jobComboBox.setValue(job.get());
    if (task != null)
      taskCombobox.setValue(task.get());
    if (description != null)
      descriptionField.setText(description.get());
    if (status != null)
      statusComboBox.setValue(status.get());
    if (estimateTime != null)
      estimateTimeField.setText(estimateTime.get());
    if (startTime != null)
      startTimeField.setText(startTime.get());
    if (endTime != null)
      endTimeField.setText(endTime.get());
    if (totalTimeSpent != null)
      totalTimeField.setText(totalTimeSpent.get());
    if (dateCreated != null)
      dateCreatedField.setText(dateCreated.get());
    changeTasks();

    textFieldProperty();
  }

  private void textFieldProperty() {
    Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
    TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
      return pattern.matcher(change.getControlNewText()).matches() ? change : null;
    });

    estimateTimeField.setTextFormatter(formatter);
  }

  private List<String> getJobs() {
    return jobTaskHandler.allJobs(true).stream()
        .map(Job::getJob)
        .collect(Collectors.toList());
  }

  @FXML
  public void changeTasks() {
    String project = jobComboBox.getValue();
    Optional<Job> any = jobTaskHandler.allJobs(true).stream()
        .filter(job -> job.getJob().equals(project))
        .findAny();

    if (!any.isPresent()) {
      jobComboBox.setValue("Select a Job");
      taskCombobox.setValue("Select a Task");
      return;
    }
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
    String taskValue = taskCombobox.getValue();
    String taskTitle = taskValue.equalsIgnoreCase("Custom Task")
        ? customTaskField.getText()
        : taskValue;
//    BusinessValue businessValue = getBusinessValue();
//    Project project = getProject();


    XpmTask xpmTask = XpmTask.builder()
        .task(taskTitle)
        .job(jobComboBox.getValue())
        .billable("Y")
        .estimateTime(estimateTimeField.getText())
//        .invoiceTypeId(taskBeingEdited.getInvoiceTypeAdapter().get())
//        .assigneeId(taskBeingEdited.getAssigneeAdapter().get())
        .totalTimeSpent(totalTimeField.getText())
//        .businessValueId(taskBeingEdited.getBusinessValueId().get())
        .status(Status.convert(statusComboBox.getValue().toString()).getStatus())
        .description(description)
        .percentCompleted(taskBeingEdited.getPercentCompleted().get())
        .dateCreated(dateCreatedField.getText())
        .startTime(startTimeField.getText())
        .endTime(endTimeField.getText())
        .build();

    StringProperty df = taskBeingEdited.getDateFinished();
    if (df != null)
      xpmTask.setDateFinished(df.get());

    LongProperty id1 = taskBeingEdited.getId();
    if (id1 != null)
      xpmTask.setId(id1.get());

    XpmTaskPostEntity xpmTaskPostEntity = new XpmHelper().helpMe(xpmTask);
    xpmTaskHandler.edit(xpmTaskPostEntity);
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
    return jobComboBox.getSelectionModel().isEmpty() || taskCombobox.getSelectionModel().isEmpty() || descriptionField.getText().isEmpty();
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
//      descriptionLabel.setLayoutY(203);
//      descriptionField.setLayoutY(203);
    } else {

      customTaskField.setVisible(true);
      customTaskLabel.setVisible(true);
//      descriptionLabel.setLayoutY(246);
//      descriptionField.setLayoutY(246);
    }
  }

}