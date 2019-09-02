package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.TaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.TaskConnector;
import inc.pabacus.TaskMetrics.desktop.breakTimer.BreakPresenter;
import inc.pabacus.TaskMetrics.desktop.edit.EditView;
import inc.pabacus.TaskMetrics.desktop.edit.EditableTaskHolder;
import inc.pabacus.TaskMetrics.desktop.jobs.JobTaskIdHolder;
import inc.pabacus.TaskMetrics.desktop.jobs.JobsView;
import inc.pabacus.TaskMetrics.desktop.newTask.NewTaskView;
import inc.pabacus.TaskMetrics.desktop.taskTimesheet.TaskTimesheetView;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.WindowChecker;
import inc.pabacus.TaskMetrics.utils.logs.LogHelper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TasksPresenter implements Initializable {

  private static final Logger logger = Logger.getLogger(TasksPresenter.class);
  private LogHelper logHelper;

  @FXML
  private AnchorPane mainPane;
  @FXML
  private JFXButton editButton;
  @FXML
  private JFXComboBox<String> statusBox;
  @FXML
  private Hyperlink refreshButton;
  @FXML
  private Hyperlink startLink;
  @FXML
  private TableView<TaskAdapter> tasksTable;
  //  @FXML
//  private JFXTextField sortTask;
  @FXML
  private JFXComboBox<String> timeBox;

  private ActivityHandler activityHandler;
  private TaskConnector taskConnector;

  public TasksPresenter() {
    logHelper = new LogHelper(logger);
    activityHandler = BeanManager.activityHandler();
    taskConnector = new TaskConnector();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    logHelper.logInfo("Initializing tasks page", null);

    //Start button will be disable when you click it without choosing a task
    startLink.disableProperty().bind(Bindings.isEmpty(tasksTable.getSelectionModel().getSelectedItems()));

    statusBox.setValue("All");
    timeBox.setValue("Today");

    TableColumn<TaskAdapter, String> dateCreated = new TableColumn<>("Date Created");
    dateCreated.setCellValueFactory(param -> param.getValue().getDateCreated());

    TableColumn<TaskAdapter, String> projectName = new TableColumn<>("Job");
    projectName.setCellValueFactory(param -> param.getValue().getJob());

    TableColumn<TaskAdapter, String> billableHours = new TableColumn<>("Time");
    billableHours.setCellValueFactory(param -> param.getValue().getTotalTimeSpent());

    TableColumn<TaskAdapter, String> task = new TableColumn<>("Task");
    task.setCellValueFactory(param -> param.getValue().getTask());

    TableColumn<TaskAdapter, String> status = new TableColumn<>("State");
    status.setCellValueFactory(param -> param.getValue().getStatus());

    TableColumn<TaskAdapter, String> description = new TableColumn<>("Description");
    description.setCellValueFactory(param -> param.getValue().getDescription());


    tasksTable.getColumns().addAll(dateCreated, projectName, task,
                                   status, billableHours, description);

    initTasksTable();
//    filtering(); Search Function
//    initTaskSheet(); time sheet has been extracted
    refreshingService(); //deactivated for maintenance

    statusBox.getItems().addAll(new ArrayList<String>() {{
      add("In Progress");
      add("Pending");
      add("Done");
      add("All");
    }});

    timeBox.getItems().addAll(new ArrayList<String>() {{
      add("Today");
      add("Last Week");
      add("Last Month");
      add("Current Month");
      add("All");
    }});

  }

//  private void filtering() {
//
//    sortTask.setOnKeyReleased(e -> {
//      sortTask.textProperty().addListener((observable, oldValue, newValue) -> {
//        ObservableList<TaskAdapter> masterData = FXCollections.observableArrayList(getAllTasks());
//        FilteredList<TaskAdapter> filteredList = new FilteredList<>(masterData, p -> true);
//        filteredList.setPredicate(sortTask -> {
//          // If filter text is empty, display all persons.
//
//          // Compare first name and last name of every person with filter text.
//          String lowerCaseFilter = newValue.toLowerCase();
//
//          if (newValue.isEmpty()) {
//            return true;
//          } else if (sortTask.getDescription().get().toLowerCase().contains(lowerCaseFilter)) {
//            return true; // Filter matches first name.
//          } else return sortTask.getTask().get().toLowerCase().contains(lowerCaseFilter); // Filter matches last name.
//          // Does not match.
//        });
//        SortedList<TaskAdapter> sortedData = new SortedList<>(filteredList);
//        sortedData.comparatorProperty().bind(tasksTable.comparatorProperty());
//        tasksTable.setItems(sortedData);
//      });
////      refreshTasks();
//
//
//    });
//

  //  }
  @FXML
  void tableClicked(MouseEvent event) {
    event.consume();
    startLink.disableProperty().bind(Bindings.isEmpty(tasksTable.getSelectionModel().getSelectedItems()).or(Bindings.when(new SimpleBooleanProperty(BreakPresenter.windowIsOpen)).then(true).otherwise(false)));
  }

  @FXML
  public void updateTable() {
    String value = statusBox.getValue();
    String time = timeBox.getValue();
    ObservableList<TaskAdapter> tasksByStatus = value.equalsIgnoreCase("All") && time.equalsIgnoreCase("All") ? FXCollections.observableArrayList(getAllTasks()) : getTasksByStatus();
    tasksTable.setItems(tasksByStatus);
  }

  @FXML
  private void viewWorkSummary() {
    GuiManager.getInstance().displayView(new TaskTimesheetView());
  }

  @FXML
  public void startTask() {
    logHelper.logInfo("Start tracking a task", null);

    if (TrackHandler.getSelectedTask() != null)
      return;

    TaskAdapter selectedItem = tasksTable.getSelectionModel().getSelectedItem();

/*  Commented out to be able to start completed tasks
    if (selectedItem.getStatus().get().equals(Status.DONE.getStatus())) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not Allowed");
      alert.setHeaderText(null);
      alert.setContentText("This task has already been completed");
      alert.showAndWait();
      return;
    }
*/
    activityHandler.saveTimestamp(Activity.BUSY);
    String newDescription = updateDescription(selectedItem.getDescription().get());
    selectedItem.setDescription(new SimpleStringProperty(newDescription));
    TrackHandler.setSelectedTask(selectedItem);
    GuiManager.getInstance().displayAlwaysOnTop(new TrackerView());
    ((Stage) startLink.getScene().getWindow()).setIconified(true);
  }

  private String updateDescription(String currentDescription) {
    TextInputDialog dialog = new TextInputDialog(currentDescription);
    dialog.setTitle("Update Description");
    dialog.setHeaderText(null);
    dialog.setGraphic(null);
    dialog.setContentText("Update task description");
    Optional<String> result = dialog.showAndWait();
    return result.get();
  }

  @FXML
  public void refreshTask() {
    refreshTasks();
  }

  @FXML
  public void newTask() {
    if (!WindowChecker.isNewTaskWindowOpen()) {
      WindowChecker.setNewTaskWindowOpen(true);
      GuiManager.getInstance().displayViewWithOnCloseRequest(new NewTaskView(), () -> WindowChecker.setNewTaskWindowOpen(false));
    }
  }

  @FXML
  public void editTask() {
    EditableTaskHolder.setTask(tasksTable.getSelectionModel().getSelectedItem());
    GuiManager.getInstance().displayView(new EditView());
  }

  @FXML
  public void viewJobsPage() {
    updateDynamicPaneContent(new JobsView().getView());
  }

  private void initTasksTable() {
    logHelper.logInfo("Initializing tasks table", null);
    tasksTable.setItems(FXCollections.observableArrayList(getAllTasks()));
  }

  private ObservableList<TaskAdapter> getTasksByStatus() {
    String status = statusBox.getValue();
    String time = timeBox.getValue();
    List<TaskAdapter> backLogs;

    switch (time) {
      case "Last Week":
        backLogs = getAllTasks().stream()
            .filter(backLog -> {
              return lovelyDay(status, backLog, 7);
            })
            .collect(Collectors.toList());
        break;
      case "Last Month":
        backLogs = getAllTasks().stream()
            .filter(backLog -> {
              return lovelyDay(status, backLog, 30);
            })
            .collect(Collectors.toList());
        break;
      case "Current Month":
        backLogs = getAllTasks().stream()
            .filter(backLog -> {
              StringProperty currentSTatus = backLog.getStatus();
              if (currentSTatus == null)
                currentSTatus = new SimpleStringProperty("");
              Month date = LocalDate.now().getMonth();
              Month getDate = LocalDate.parse(backLog.getDateCreated().get()).getMonth();
              if (status.equalsIgnoreCase("all"))
                return date.equals(getDate);
              return date.equals(getDate) && currentSTatus.get().equalsIgnoreCase(status);
            })
            .collect(Collectors.toList());
        break;
      case "Today":
        backLogs = getAllTasks().stream()
            .filter(backLog -> {
              StringProperty currentSTatus = backLog.getStatus();
              if (currentSTatus == null)
                currentSTatus = new SimpleStringProperty("");
              if (status.equalsIgnoreCase("all"))
                return backLog.getDateCreated().get().equals(LocalDate.now().toString());
              return backLog.getDateCreated().get().equals(LocalDate.now().toString()) && currentSTatus.get().equalsIgnoreCase(status);

            })
            .collect(Collectors.toList());
        break;
      case "All":
      default:
        backLogs = getAllTasks().stream()
            .filter(backlog -> {
              StringProperty currentSTatus = backlog.getStatus();
              if (currentSTatus == null)
                currentSTatus = new SimpleStringProperty("");
              return currentSTatus.get().equalsIgnoreCase(status);
            })
            .collect(Collectors.toList());
        break;
    }
    return FXCollections.observableArrayList(backLogs);
  }

  private boolean lovelyDay(String status, TaskAdapter backLog, int i) {
    StringProperty currentSTatus = backLog.getStatus();
    if (currentSTatus == null)
      currentSTatus = new SimpleStringProperty("");
    LocalDate date = LocalDate.now().minusDays(i);
    LocalDate getDate = LocalDate.parse(backLog.getDateCreated().get());
    if (status.equalsIgnoreCase("all"))
      return getDate.isAfter(date);
    return getDate.isAfter(date) && currentSTatus.get().equalsIgnoreCase(status);
  }

  private void refreshTasks() {
    List<TableColumn<TaskAdapter, ?>> sortOrder = new ArrayList<>(tasksTable.getSortOrder());
    int i = tasksTable.getSelectionModel().getSelectedIndex();
    Platform.runLater(() -> {
      initTasksTable();
//      refreshTables();
      tasksTable.getSortOrder().clear();
      tasksTable.getSortOrder().addAll(sortOrder);
      tasksTable.getSelectionModel().select(i);
    });
  }

  private void refreshTables() {
    if (statusBox.getValue().equals("All") && timeBox.getValue().equals("All"))
      initTasksTable();
    else
      updateTable();
  }

  /**
   * Auto refresh feature. To activate, just call this method.
   * Currently disabled due to some trouble with JavaFX Table sorting
   */
  private void refreshingService() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(this::refreshTasks, 60L, 60L, TimeUnit.SECONDS);
  }

  private List<TaskAdapter> getAllTasks() {
    Long id = JobTaskIdHolder.getId();
    List<Task> allTasks = taskConnector.findByJobTask(id);
    return FXCollections
        .observableArrayList(allTasks.stream()
                                 .map(TaskAdapter::new)
                                 .collect(Collectors.toList()));
  }

  private void updateDynamicPaneContent(Parent parent) {
    AnchorPane.setTopAnchor(parent, 0.0);
    AnchorPane.setLeftAnchor(parent, 0.0);
    AnchorPane.setBottomAnchor(parent, 0.0);
    AnchorPane.setRightAnchor(parent, 0.0);

    mainPane.getChildren().clear();
    mainPane.getChildren().add(parent);
  }
}
