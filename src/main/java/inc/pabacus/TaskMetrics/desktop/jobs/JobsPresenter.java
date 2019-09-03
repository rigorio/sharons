package inc.pabacus.TaskMetrics.desktop.jobs;

import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.desktop.newTask.JobHolder;
import inc.pabacus.TaskMetrics.desktop.taskTimesheet.TaskTimesheetView;
import inc.pabacus.TaskMetrics.desktop.tasks.TasksView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.logs.LogHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class JobsPresenter implements Initializable {

  private static final Logger logger = Logger.getLogger(JobsPresenter.class);
  private LogHelper logHelper;

  @FXML
  private AnchorPane dynamicContentPane;
  @FXML
  private TableView<JobTaskAdapter> jobsTable;
  @FXML
  private JFXComboBox<String> jobComboBox;
  @FXML
  private Hyperlink addJobLink;

  private JobTaskHandler jobTaskHandler = new JobTaskHandler();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    logHelper = new LogHelper(logger);
    logHelper.logInfo("Initializing jobs page", null);
    getJobs().forEach(job -> jobComboBox.getItems().add(job.getJob()));
    initialize();
  }

  @FXML
  public void viewByJob() {

  }

  @FXML
  public void addJob() {
    String value = jobComboBox.getValue();
    Job job = getJobs().stream()
        .filter(j -> j.getJob().equals(value))
        .findFirst()
        .get();
    jobTaskHandler.createJobTask(job.getId(), null, null);
    init();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Job Created!");
    alert.showAndWait();
  }

  private void initialize() {

    TableColumn<JobTaskAdapter, String> jobName = new TableColumn<>("Job");
    jobName.setCellValueFactory(param -> {
      JobTaskAdapter job = param.getValue();
      String jobNumber = job.getJobNumber().get();
      String jN = job.getJobName().get();
      return new SimpleStringProperty(jobNumber + " " + jN);
    });

    TableColumn<JobTaskAdapter, String> dateCreated = new TableColumn<>("Description");
    dateCreated.setCellValueFactory(param -> param.getValue().getDescription());

    TableColumn<JobTaskAdapter, String> status = new TableColumn<>("Status");
    status.setCellValueFactory(param -> param.getValue().getStatus());

    TableColumn<JobTaskAdapter, String> percentage = new TableColumn<>("Percentage");
    percentage.setCellValueFactory(param -> param.getValue().getPercentage());

    jobsTable.getColumns().addAll(jobName,
                                  dateCreated,
                                  status,
                                  percentage);

    init();

    jobsTable.setOnMouseClicked(event -> {
      JobTaskAdapter selectedItem = jobsTable.getSelectionModel().getSelectedItem();
      logHelper.logInfo("Selected a job", selectedItem);
      JobTaskIdHolder.setId(selectedItem.getId().get());
      JobHolder.setJob(selectedItem.getJobName().get());
      updateDynamicPaneContent(new TasksView().getView());

    });

  }

  private void init() {
    jobsTable.setItems(FXCollections.observableArrayList(getJobTasks()));
  }

  private void setTable() {

  }

  private List<Job> getJobs() {
    return jobTaskHandler.allJobs(true);
  }

  private List<JobTaskAdapter> getJobTasks() {
    return jobTaskHandler.allJobTasks()
        .stream()
        .map(JobTaskAdapter::new)
        .collect(Collectors.toList());
  }

  private void updateDynamicPaneContent(Parent parent) {
    AnchorPane.setTopAnchor(parent, 0.0);
    AnchorPane.setLeftAnchor(parent, 0.0);
    AnchorPane.setBottomAnchor(parent, 0.0);
    AnchorPane.setRightAnchor(parent, 0.0);

    dynamicContentPane.getChildren().clear();
    dynamicContentPane.getChildren().add(parent);
  }

  @FXML
  private void viewWorkSummary() {
    GuiManager.getInstance().displayView(new TaskTimesheetView());
  }
}
