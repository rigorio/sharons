package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.project.ProjectFXAdapter;
import inc.pabacus.TaskMetrics.api.project.ProjectHandler;
import inc.pabacus.TaskMetrics.api.project.ProjectService;
import inc.pabacus.TaskMetrics.api.tasks.*;
import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.desktop.newTask.NewTaskView;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TasksPresenter implements Initializable {

  @FXML
  private JFXComboBox<String> statusBox;
  @FXML
  private Label totalbillable;
  @FXML
  private Label totalNonBillable;
  @FXML
  private Label totalHours;
  @FXML
  private Label totalPercentBillable;
  @FXML
  private Label totalInvoice;

  @FXML
  private JFXTextField titleText;
  @FXML
  private JFXComboBox<String> progressText;
  @FXML
  private JFXComboBox<String> statusText;
  @FXML
  private JFXComboBox<String> priorityText;
  @FXML
  private JFXTextArea descriptionText;
  @FXML
  private JFXButton deleteTask;
  @FXML
  private JFXButton saveTask;

  @FXML
  private TableView<ProjectFXAdapter> taskTimesheet;

  @FXML
  private JFXButton refreshButton;
  @FXML
  private JFXButton startButton;

  @FXML
  private TableView<TaskFXAdapter> tasksTable;

  private TaskHandler taskHandler;

  private ProjectService projectService;

  public TasksPresenter() {
    taskHandler = new TaskHandler(new TaskWebRepository());
    projectService = new ProjectHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    statusBox.setValue("Pending");
    startButton.setDisable(true);
    saveTask.setDisable(true);
    deleteTask.setDisable(true);

    TableColumn<TaskFXAdapter, String> projectName = new TableColumn<>("Project");
    projectName.setCellValueFactory(param -> param.getValue().getProjectName());

    TableColumn<TaskFXAdapter, String> startTime = new TableColumn<>("Start Time");
    startTime.setCellValueFactory(param -> param.getValue().getStartTime());

    TableColumn<TaskFXAdapter, String> endTime = new TableColumn<>("End Time");
    endTime.setCellValueFactory(param -> param.getValue().getEndTime());

    TableColumn<TaskFXAdapter, String> billable = new TableColumn<>("Billable?");
    billable.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBillable().get() ? "Y" : "N"));

    TableColumn<TaskFXAdapter, String> billableHours = new TableColumn<>("Billable Hours");
    billableHours.setCellValueFactory(param -> {
      TaskFXAdapter adapter = param.getValue();
      String b = adapter.getBillable().get() ? "" + adapter.getTimeSpent().get() : "0";
      return new SimpleStringProperty(b);
    });

    TableColumn<TaskFXAdapter, String> nonBillableHours = new TableColumn<>("Non-Billable Hours");
    nonBillableHours.setCellValueFactory(param -> {
      TaskFXAdapter adapter = param.getValue();
      String b = !adapter.getBillable().get() ? "" + adapter.getTimeSpent().get() : "0";
      return new SimpleStringProperty(b);
    });

    TableColumn<TaskFXAdapter, String> description = new TableColumn<>("Task");
    description.setCellValueFactory(param -> param.getValue().getTitle());


    tasksTable.getColumns().addAll(projectName, description, startTime, endTime,
                                   billableHours, nonBillableHours, billable);

    initTasksTable();

    initTaskSheet();
    initEditables();
    refreshingService();
//    hideHeaders();
  }

  @FXML
  public void wowStatusBoxChange() {
    String value = statusBox.getValue();
    ObservableList<TaskFXAdapter> tasksByStatus;
    if (value.equals("All")) {
      tasksByStatus = FXCollections.observableArrayList(getAllTasks());
    } else {
      tasksByStatus = getTasksByStatus(value);
    }
    tasksTable.setItems(tasksByStatus);
  }

  @FXML
  public void deleteTask() {
    TaskFXAdapter selectedItem = tasksTable.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirmation");
      alert.setHeaderText("Delete task?");
      alert.setContentText("Task #" + selectedItem.getId().get());

      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == ButtonType.OK) {
        taskHandler.deleteTask(selectedItem.getId().getValue());
        refreshTasks();
      }
    }
  }

  @FXML
  public void saveTask() {
    Task task = new Task(tasksTable.getSelectionModel().getSelectedItem());
    saveTask(task);
    refreshTasks();
  }

  @FXML
  public void startTask() {
    TaskFXAdapter selectedItem = tasksTable.getSelectionModel().getSelectedItem();
    if (!selectedItem.getStatus().get().equals("Pending") && !selectedItem.getStatus().get().equals("In Progress")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not Allowed");
      alert.setHeaderText(null);
      alert.setContentText("This task has already been marked has done. Please select a task under \"Pending\"");
      alert.showAndWait();
      return;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
    selectedItem.setStartTime(new SimpleStringProperty(formatter.format(LocalTime.now())));
    startButton.setDisable(true);
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
  public void viewScreenShot() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../screenshot/ScreenShot.fxml"));
      Parent root1 = (Parent) fxmlLoader.load();
      Stage stage = new Stage();
      stage.getIcons().add(new Image("/img/PabacusLogo.png"));
      stage.setTitle("Screen Shots");
      stage.setScene(new Scene(root1));
      stage.show();

    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private void initTaskSheet() {
    TableColumn<ProjectFXAdapter, String> projectName = new TableColumn<>("Project");
    projectName.setCellValueFactory(param -> param.getValue().getProjectName());

    TableColumn<ProjectFXAdapter, String> billableHours = new TableColumn<>("Billable Hours");
    billableHours.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getBillable().get()));

    TableColumn<ProjectFXAdapter, String> nonBillableHours = new TableColumn<>("Non-Billable Hours");
    nonBillableHours.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getNonBillable().get()));

    TableColumn<ProjectFXAdapter, String> totalHours = new TableColumn<>("Total Hours");
    totalHours.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getTotalHours().get()));

    TableColumn<ProjectFXAdapter, String> billable = new TableColumn<>("Billable");
    billable.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getPercentBillable().get() + "%"));

    TableColumn<ProjectFXAdapter, String> invoiceAmount = new TableColumn<>("Invoice Amount");
    invoiceAmount.setCellValueFactory(param -> new SimpleStringProperty("$" + param.getValue().getInvoiceAmount().get()));


    taskTimesheet.getColumns().addAll(projectName, billableHours, nonBillableHours,
                                      totalHours, billable, invoiceAmount);
    initTaskTimeSheet();

    totalbillable.setText("7.49");
    totalNonBillable.setText("1.92");
    this.totalHours.setText("9.41");
    totalPercentBillable.setText("80%");
    totalInvoice.setText("$491.00");

  }

  private void initEditables() {

    progressText.getItems().addAll(new ArrayList<String>() {{
      add("0");
      add("25");
      add("50");
      add("75");
      add("100");
    }});

    ArrayList<String> statuses = new ArrayList<String>() {{
      add("Pending");
      add("In Progress");
      add("Done");
    }};
    statusText.getItems().addAll(statuses);
    statuses.add("All");
    statusBox.getItems().addAll(statuses);


    priorityText.getItems().addAll(new ArrayList<String>() {{
      add("1");
      add("2");
      add("3");
      add("4");
      add("5");
    }});

    tasksTable.getSelectionModel().selectedItemProperty().addListener(item -> {
      TaskFXAdapter selectedItem = tasksTable.getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        Task task = new Task(selectedItem);
        titleText.setText(task.getTitle());
        descriptionText.setText(task.getDescription());

        Progress progress = task.getProgress();
        progressText.setValue(progress != null ? progress.getProgress().toString() : "");

        Status status = task.getStatus();
        statusText.setValue(status != null ? status.getStatus() : "");

        Priority priority = task.getPriority();
        priorityText.setValue(priority != null ? priority.getPriority().toString() : "");


        // TODO extract this from listener for more readability
        saveTask.setDisable(false);
        deleteTask.setDisable(false);
        if (TrackHandler.getSelectedTask() == null)
          startButton.setDisable(false);
      } else {
        saveTask.setDisable(true);
        deleteTask.setDisable(true);
        startButton.setDisable(true);
      }
    });


  }

  private void saveTask(Task task) {
    // if title empty, prompt and do not execute
    task.setTitle(titleText.getText());
    checkOtherValues(task);
    taskHandler.createTask(task);
  }

  private void checkOtherValues(Task task) {
    if (descriptionText.getText() != null && !descriptionText.getText().equals(""))
      task.setDescription(descriptionText.getText());

    if (progressText.getValue() != null && !progressText.getValue().equals(""))
      task.setProgress(Progress.convert(Integer.valueOf(progressText.getValue())));

    if (statusText.getValue() != null && !statusText.getValue().equals(""))
      task.setStatus(Status.convert(statusText.getValue()));

    if (priorityText.getValue() != null && !priorityText.getValue().equals(""))
      task.setPriority(Priority.convert(Integer.valueOf(priorityText.getValue())));
  }

  private void initTaskTimeSheet() {
    taskTimesheet.setItems(getProjects());
  }

  private ObservableList<ProjectFXAdapter> getProjects() {
    List<ProjectFXAdapter> projects = projectService.getAllFXProjects();
    return FXCollections.observableArrayList(projects);
  }

  private void initTasksTable() {
    tasksTable.setItems(FXCollections.observableArrayList(getAllTasks()));
  }

  private ObservableList<TaskFXAdapter> getTasksByStatus(String status) {
    List<TaskFXAdapter> backLogs = getAllTasks().stream()
        .filter(backlog -> backlog.getStatus().get().equalsIgnoreCase(status))
        .collect(Collectors.toList());
    return FXCollections.observableArrayList(backLogs);
  }


  /**
   * This is also a quick hack... will explain possibly in the future when i
   * do not forget
   */
  private void refreshTasks() {
    int i = tasksTable.getSelectionModel().getSelectedIndex();
    String d = descriptionText.getText();
    String tt = titleText.getText();
    String pro = progressText.getValue();
    String sta = statusText.getValue();
    String prio = priorityText.getValue();
    refreshTables();
    tasksTable.getSelectionModel().select(i);
    descriptionText.setText(d);
    titleText.setText(tt);
    progressText.setValue(pro);
    statusText.setValue(sta);
    priorityText.setValue(prio);
    descriptionText.positionCaret(descriptionText.getLength());
  }

  private void refreshTables() {
    if (statusBox.getValue().equals("All"))
      initTasksTable();
    else
      tasksTable.setItems(getTasksByStatus(statusBox.getValue()));

    initTaskTimeSheet();
  }

  private void refreshingService() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(this::refreshTasks, 5L, 5L, TimeUnit.SECONDS);
  }

  private ObservableList<TaskFXAdapter> getTasksToday() {
    String dateToday = LocalDate.now().toString();
    List<TaskFXAdapter> tasks = getAllTasks().stream()
        .filter(task -> task.getDateCompleted() != null && task.getDateCompleted().get().equalsIgnoreCase(dateToday))
        .collect(Collectors.toList());
    return FXCollections.observableArrayList(tasks);
  }


  private List<TaskFXAdapter> getAllTasks() {
    return FXCollections
        .observableArrayList(taskHandler.getAllTasks().stream()
                                 .map(TaskFXAdapter::new)
                                 .collect(Collectors.toList()));
  }

  private void hideHeaders() {
    hideHeaders(tasksTable);
  }

  private void hideHeaders(TableView<TaskFXAdapter> doneTasksTable) {
    doneTasksTable.widthProperty().addListener((observable, oldValue, newValue) -> {
      Pane header = (Pane) doneTasksTable.lookup("TableHeaderRow");
      if (header.isVisible()) {
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
      }
    });
  }
}
