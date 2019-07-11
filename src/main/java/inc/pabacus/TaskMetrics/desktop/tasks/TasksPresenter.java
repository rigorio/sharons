package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.desktop.breakTimer.BreakPresenter;
import inc.pabacus.TaskMetrics.desktop.edit.EditView;
import inc.pabacus.TaskMetrics.desktop.edit.EditableTaskHolder;
import inc.pabacus.TaskMetrics.desktop.newTask.NewTaskView;
import inc.pabacus.TaskMetrics.desktop.taskTimesheet.TaskTimesheetView;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
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
  @FXML
  private JFXTextField sortTask;
  @FXML
  private JFXComboBox<String> timeBox;

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
    timeBox.setValue("All");

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
    filtering();

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

  private void filtering() {

    sortTask.setOnKeyReleased(e -> {
      sortTask.textProperty().addListener((observable, oldValue, newValue) -> {
        ObservableList<XpmTaskAdapter> masterData = FXCollections.observableArrayList(getAllTasks());
        FilteredList<XpmTaskAdapter> filteredList = new FilteredList<>(masterData, p -> true);
        filteredList.setPredicate(sortTask -> {
          // If filter text is empty, display all persons.

          // Compare first name and last name of every person with filter text.
          String lowerCaseFilter = newValue.toLowerCase();

          if (newValue.isEmpty()) {
            return true;
          } else if (sortTask.getDescription().get().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches first name.
          } else return sortTask.getTask().get().toLowerCase().contains(lowerCaseFilter); // Filter matches last name.
          // Does not match.
        });
        SortedList<XpmTaskAdapter> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(tasksTable.comparatorProperty());
        tasksTable.setItems(sortedData);
      });
//      refreshTasks();


    });

  }

  @FXML
  void tableClicked(MouseEvent event) {
    event.consume();
    startButton.disableProperty().bind(Bindings.isEmpty(tasksTable.getSelectionModel().getSelectedItems()).or(Bindings.when(new SimpleBooleanProperty(BreakPresenter.windowIsOpen)).then(true).otherwise(false)));
  }

  @FXML
  public void updateTable() {
    String value = statusBox.getValue();
    String time = timeBox.getValue();
    ObservableList<XpmTaskAdapter> tasksByStatus = value.equalsIgnoreCase("All") && time.equalsIgnoreCase("All") ? FXCollections.observableArrayList(getAllTasks()) : getTasksByStatus();
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
    ((Stage) startButton.getScene().getWindow()).setIconified(true);
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

  private ObservableList<XpmTaskAdapter> getTasksByStatus() {
    String status = statusBox.getValue();
    String time = timeBox.getValue();
    List<XpmTaskAdapter> backLogs;

    switch (time) {
      case "Last Week":
        backLogs = getAllTasks().stream()
            .filter(backLog -> {
              StringProperty currentSTatus = backLog.getStatus();
              if (currentSTatus == null)
                currentSTatus = new SimpleStringProperty("");
              LocalDate date = LocalDate.now().minusDays(7);
              LocalDate getDate = LocalDate.parse(backLog.getDateCreated().get());
              if (status.equalsIgnoreCase("all"))
                return getDate.isAfter(date);
              return getDate.isAfter(date) && currentSTatus.get().equalsIgnoreCase(status);
            })
            .collect(Collectors.toList());
        break;
      case "Last Month":
        backLogs = getAllTasks().stream()
            .filter(backLog -> {
              StringProperty currentSTatus = backLog.getStatus();
              if (currentSTatus == null)
                currentSTatus = new SimpleStringProperty("");
              LocalDate date = LocalDate.now().minusDays(30);
              LocalDate getDate = LocalDate.parse(backLog.getDateCreated().get());
              if (status.equalsIgnoreCase("all"))
                return getDate.isAfter(date);
              return getDate.isAfter(date) && currentSTatus.get().equalsIgnoreCase(status);
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

  private void refreshTasks() {
    int i = tasksTable.getSelectionModel().getSelectedIndex();
    refreshTables();
    tasksTable.getSelectionModel().select(i);
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
