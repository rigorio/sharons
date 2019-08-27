package inc.pabacus.TaskMetrics.desktop.jobs;

import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.desktop.newTask.JobHolder;
import inc.pabacus.TaskMetrics.desktop.tasks.TasksView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class JobsPresenter implements Initializable {

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
  }

  private void initialize() {
    TableColumn<JobTaskAdapter, String> dateCreated = new TableColumn<>("Id");
    dateCreated.setCellValueFactory(param -> param.getValue().getJobNumber());

    TableColumn<JobTaskAdapter, String> jobName = new TableColumn<>("Job");
    jobName.setCellValueFactory(param -> param.getValue().getJobName());

    TableColumn<JobTaskAdapter, String> status = new TableColumn<>("Status");
    status.setCellValueFactory(param -> param.getValue().getStatus());

    TableColumn<JobTaskAdapter, String> percentage = new TableColumn<>("Percentage");
    percentage.setCellValueFactory(param -> param.getValue().getPercentage());

    jobsTable.getColumns().addAll(dateCreated,
                                  jobName,
                                  status,
                                  percentage);

    init();

    jobsTable.setOnMouseClicked(event -> {
      JobTaskAdapter selectedItem = jobsTable.getSelectionModel().getSelectedItem();
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
}
