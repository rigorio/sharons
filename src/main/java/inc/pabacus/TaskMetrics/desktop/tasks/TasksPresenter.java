package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.desktop.edit.EditView;
import inc.pabacus.TaskMetrics.desktop.edit.EditableTaskHolder;
import inc.pabacus.TaskMetrics.desktop.newTask.NewTaskView;
import inc.pabacus.TaskMetrics.desktop.taskTimesheet.TaskTimesheetView;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TasksPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private JFXButton editButton;
  @FXML
  private JFXComboBox<String> statusBox;
  @FXML
  private JFXButton refreshButton;
  @FXML
  private JFXButton startButton;
  @FXML
  private TableView<XpmTaskAdapter> tasksTable;

  private ActivityHandler activityHandler;
  private XpmTaskWebHandler xpmTaskHandler;

  public TasksPresenter() {
    activityHandler = BeanManager.activityHandler();
    xpmTaskHandler = BeanManager.xpmTaskHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //Start button will be disable when you click it without choosing a task
    startButton.disableProperty().bind(Bindings.isEmpty(tasksTable.getSelectionModel().getSelectedItems()));

    statusBox.setValue("All");

    TableColumn<XpmTaskAdapter, String> dateCreated = new TableColumn<>("Date Created");
    dateCreated.setCellValueFactory(param -> param.getValue().getDateCreated());

    TableColumn<XpmTaskAdapter, String> projectName = new TableColumn<>("Job");
    projectName.setCellValueFactory(param -> param.getValue().getJob());

    TableColumn<XpmTaskAdapter, String> billableHours = new TableColumn<>("Time");
    billableHours.setCellValueFactory(param -> param.getValue().getTotalTimeSpent());

    TableColumn<XpmTaskAdapter, String> task = new TableColumn<>("Task");
    task.setCellValueFactory(param -> param.getValue().getTask());

    TableColumn<XpmTaskAdapter, String> status = new TableColumn<>("State");
    status.setCellValueFactory(param -> param.getValue().getStatus());

    TableColumn<XpmTaskAdapter, String> description = new TableColumn<>("Description");
    description.setCellValueFactory(param -> param.getValue().getDescription());


    tasksTable.getColumns().addAll(dateCreated, projectName, task,
                                   status, billableHours, description);

    initTasksTable();

//    initTaskSheet(); time sheet has been extracted
//    refreshingService(); deactivated for maintenance
    statusBox.getItems().addAll(new ArrayList<String>() {{
      add("In Progress");
      add("Pending");
      add("Done");
      add("All");
    }});

  }

  @FXML
  public void updateTable() {
    String value = statusBox.getValue();
    ObservableList<XpmTaskAdapter> tasksByStatus = value.equalsIgnoreCase("All")
        ? FXCollections.observableArrayList(getAllTasks())
        : getTasksByStatus(value);
    tasksTable.setItems(tasksByStatus);
  }

  @FXML
  private void viewWorkSummary() {
    GuiManager.getInstance().displayView(new TaskTimesheetView());
  }

  @FXML
  public void startTask() {

    if (TrackHandler.getSelectedTask() != null)
      return;

    activityHandler.saveActivity(Activity.BUSY);
    XpmTaskAdapter selectedItem = tasksTable.getSelectionModel().getSelectedItem();
    if (selectedItem.getStatus().get().equals(Status.DONE.getStatus())) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not Allowed");
      alert.setHeaderText(null);
      alert.setContentText("This task has already been completed");
      alert.showAndWait();
      return;
    }
    TrackHandler.setSelectedTask(selectedItem);
    GuiManager.getInstance().displayAlwaysOnTop(new TrackerView());
  }

  @FXML
  public void refreshTask() {
    refreshTasks();
  }

  @FXML
  public void newTask() {
    GuiManager.getInstance().displayView(new NewTaskView());
  }

  @FXML
  public void editTask() {
    EditableTaskHolder.setTask(tasksTable.getSelectionModel().getSelectedItem());
    GuiManager.getInstance().displayView(new EditView());
  }

  private void initTasksTable() {
    tasksTable.setItems(FXCollections.observableArrayList(getAllTasks()));
  }

  private ObservableList<XpmTaskAdapter> getTasksByStatus(String status) {

    List<XpmTaskAdapter> backLogs = getAllTasks().stream()
        .filter(backlog -> {
          StringProperty currentSTatus = backlog.getStatus();
          if (currentSTatus == null)
            currentSTatus = new SimpleStringProperty("");
          return currentSTatus.get().equalsIgnoreCase(status);
        })
        .collect(Collectors.toList());
    return FXCollections.observableArrayList(backLogs);
  }

  private void refreshTasks() {
    int i = tasksTable.getSelectionModel().getSelectedIndex();
    refreshTables();
    tasksTable.getSelectionModel().select(i);
  }

  private void refreshTables() {
    if (statusBox.getValue().equals("All"))
      initTasksTable();
    else
      tasksTable.setItems(getTasksByStatus(statusBox.getValue()));
  }

  /**
   * Auto refresh feature. To activate, just call this method.
   * Currently disabled due to some trouble with JavaFX Table sorting
   */
  private void refreshingService() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(this::refreshTasks, 5L, 5L, TimeUnit.SECONDS);
  }

  private List<XpmTaskAdapter> getAllTasks() {
    List<XpmTask> allTasks = xpmTaskHandler.findAll();
    return FXCollections
        .observableArrayList(allTasks.stream()
                                 .map(XpmTaskAdapter::new)
                                 .collect(Collectors.toList()));
  }
}
