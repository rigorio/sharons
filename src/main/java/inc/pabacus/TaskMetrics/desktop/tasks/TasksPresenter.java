package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.tasks.*;
import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TasksPresenter implements Initializable {

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
  private TableView<TaskFXAdapter> taskTimesheet;

  @FXML
  private JFXButton refreshButton;
  @FXML
  private JFXButton startButton;

  @FXML
  private TableView<TaskFXAdapter> tasksTable;


  private TimerService service = new TimerService();

  private ObservableList<TaskFXAdapter> backlogs = FXCollections.observableArrayList();
  private ObservableList<TaskFXAdapter> done = FXCollections.observableArrayList();

  private TaskHandler taskHandler;

  public TasksPresenter() {
    taskHandler = new TaskHandler(new TaskWebRepository());
  }

  private TableColumn<TaskFXAdapter, String> backLogsColumn = new TableColumn<>("Name");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    startButton.setDisable(true);
    saveTask.setDisable(true);
    deleteTask.setDisable(true);
    initList();
    hideTable();
    backLogsColumn.setCellValueFactory(param -> param.getValue().getTitle());
    tasksTable.getColumns().add(backLogsColumn);

    backlogs.addAll(mockTasks);
    done.addAll(mockTasks);
    initTasksTable();

    initTaskSheet();
    initEditables();
    refreshingService();
  }

  private void initEditables() {

    progressText.getItems().addAll(new ArrayList<String>() {{
      add("0");
      add("25");
      add("50");
      add("75");
      add("100");
    }});

    statusText.getItems().addAll(new ArrayList<String>() {{
      add("Backlog");
      add("In Progress");
      add("Done");
    }});

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
        startButton.setDisable(false);
      } else {
        saveTask.setDisable(true);
        deleteTask.setDisable(true);
        startButton.setDisable(true);
      }
    });

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

  private void saveTask(Task task) {
    // if title empty, prompt and do not execute
    task.setTitle(titleText.getText());
    checkOtherValues(task);
    taskHandler.saveTask(task);
  }

  private void checkOtherValues(Task task) {
    if (!descriptionText.getText().equals(""))
      task.setDescription(descriptionText.getText());

    if (!progressText.getValue().equals(""))
      task.setProgress(Progress.convert(Integer.valueOf(progressText.getValue())));

    if (!statusText.getValue().equals(""))
      task.setStatus(Status.convert(statusText.getValue()));

    if (!priorityText.getValue().equals(""))
      task.setPriority(Priority.convert(Integer.valueOf(priorityText.getValue())));
  }

  private void initTaskSheet() {
    TableColumn<TaskFXAdapter, String> title = new TableColumn<>("Title");
    title.setCellValueFactory(param -> param.getValue().getTitle());

    TableColumn<TaskFXAdapter, String> timeSpent = new TableColumn<>("Time Spent");
    timeSpent.setCellValueFactory(param -> param.getValue().getTotalTimeSpent());

    TableColumn<TaskFXAdapter, String> description = new TableColumn<>("Description");
    description.setCellValueFactory(param -> param.getValue().getDescription());

    taskTimesheet.getColumns().addAll(title, timeSpent, description);
    initTaskTimeSheet();

  }

  private void initTaskTimeSheet() {
    taskTimesheet.setItems(getTasksToday());
  }

  private void initTasksTable() {
    tasksTable.setItems(getTasks(yihiiee(), "backlog"));
  }


  @FXML
  public void startTask() {
    TaskFXAdapter selectedItem = tasksTable.getSelectionModel().getSelectedItem();
    String title = selectedItem.getTitle().get();
    startButton.setDisable(true);
    TrackHandler.setSelectedTask(selectedItem);
    GuiManager.getInstance().displayAlwaysOnTop(new TrackerView());
  }

  @FXML
  public void refreshTask() {
    refreshTasks();
  }

  /**
   * This is also a quick hack... will explain possibly in the future when i
   * do not forget
   */
  @FXML
  public void refreshTasks() {
    int i = tasksTable.getSelectionModel().getSelectedIndex();
    String d = descriptionText.getText();
    String tt = titleText.getText();
    String pro = progressText.getValue();
    String sta = statusText.getValue();
    String prio = priorityText.getValue();
    initTasksTable();
    initTaskTimeSheet();
    tasksTable.getSelectionModel().select(i);
    descriptionText.setText(d);
    titleText.setText(tt);
    progressText.setValue(pro);
    statusText.setValue(sta);
    priorityText.setValue(prio);
    descriptionText.positionCaret(descriptionText.getLength());
  }

  private void refreshingService() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(this::refreshTasks, 5L, 5L, TimeUnit.SECONDS);
  }

  private ObservableList<TaskFXAdapter> getTasksToday() {
    List<Task> allTasks = taskHandler.getAllTasks();
    String dateToday = LocalDate.now().toString();
    List<TaskFXAdapter> tasks = allTasks.stream()
        .filter(task -> task.getDateCompleted() != null && task.getDateCompleted().equalsIgnoreCase(dateToday))
        .map(TaskFXAdapter::new)
        .collect(Collectors.toList());
    return FXCollections.observableArrayList(tasks);
  }

  public void newTask(ActionEvent event) {
    TextInputDialog dialog = new TextInputDialog("");
    Image imageImage1 = new Image("/img/task.png", 50, 50, false, false);
    ImageView imageView = new ImageView(imageImage1);
    dialog.setGraphic(imageView);
//    dialog.initOwner(stage);


//    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
//    stage.getIcons().add(new Image(this.getClass().getResource("/img/PabacusLogo.png").toString()));

    dialog.setTitle("New Task:");
    dialog.setHeaderText("Add a new Task");
    dialog.setContentText("Enter a new Task:");
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      String taskName = result.get();
      Task task = new Task();
      task.setTitle(taskName);
      task.setStatus(Status.BACKLOG);
      task.setDescription("");
      taskHandler.saveTask(task);
      refreshTasks();
//      ToDo todo = new ToDo(result.get());
//      backLogsTable.getItems().add(todo);
    }
  }

  public void viewScreenShot(ActionEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ScreenShot.fxml"));
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


  /**
   * mock tasks
   */
  private List<TaskFXAdapter> mockTasks = new ArrayList<>();

  private void initList() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(new Task("task 1", "description 1", Status.BACKLOG));
    tasks.add(new Task("task 2", "description 2", Status.BACKLOG));
    tasks.add(new Task("task 3", "description 3", Status.BACKLOG));
    tasks.add(new Task("task 4", "description 4", Status.DONE));
    tasks.add(new Task("task 5", "description 5", Status.DONE));

    mockTasks = yihiiee();
  }

  private ObservableList<TaskFXAdapter> yihiiee() {
    return FXCollections
        .observableArrayList(taskHandler.getAllTasks().stream()
                                 .map(TaskFXAdapter::new)
                                 .collect(Collectors.toList()));
  }


  private ObservableList<TaskFXAdapter> getTasks(ObservableList<TaskFXAdapter> backlogs, String backlog2) {
    return FXCollections
        .observableArrayList(backlogs.stream()
                                 .filter(backlog -> backlog.getStatus().get().equalsIgnoreCase(backlog2))
                                 .collect(Collectors.toList()));
  }

  private void hideTable() {
    salikutMuKu(tasksTable);
  }

  private void salikutMuKu(TableView<TaskFXAdapter> doneTasksTable) {
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
