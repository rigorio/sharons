package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.desktop.newTask.NewTaskView;
import inc.pabacus.TaskMetrics.desktop.taskTimesheet.TaskTimesheetView;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTask;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

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
    //for fade effect
    mainPane.setOpacity(0);
    responsiveness();
    //Start button will be disable when you click it without choosing a task
    startButton.disableProperty().bind(Bindings.isEmpty(tasksTable.getSelectionModel().getSelectedItems()));

    statusBox.setValue("All");

    TableColumn<XpmTaskAdapter, String> projectName = new TableColumn<>("Job");
    projectName.setCellValueFactory(param -> param.getValue().getJob());
    TableColumn<XpmTaskAdapter, String> billableHours = new TableColumn<>("Time");
    billableHours.setCellValueFactory(param -> param.getValue().getTime());

    TableColumn<XpmTaskAdapter, String> description = new TableColumn<>("Task");
    description.setCellValueFactory(param -> param.getValue().getTitle());

    TableColumn<XpmTaskAdapter, String> status = new TableColumn<>("State");
    status.setCellValueFactory(param -> param.getValue().getStatus());


    tasksTable.getColumns().addAll(projectName, description, status, billableHours);

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
    activityHandler.saveActivity(Activity.BUSY);
    XpmTaskAdapter selectedItem = tasksTable.getSelectionModel().getSelectedItem();
    if (!selectedItem.getStatus().get().equals("Pending")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not Allowed");
      alert.setHeaderText(null);
      alert.setContentText("You can only select a task under \"Pending\"");
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

  private void initTasksTable() {
    tasksTable.setItems(FXCollections.observableArrayList(getAllTasks()));
  }

  private ObservableList<XpmTaskAdapter> getTasksByStatus(String status) {

    List<XpmTaskAdapter> backLogs = getAllTasks().stream()
        .filter(backlog -> backlog.getStatus().get().equalsIgnoreCase(status))
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

  private void responsiveness() {
    //transition
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(1000)); // 1 second
    fadeTransition.setNode(mainPane);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.play();
  }

}
