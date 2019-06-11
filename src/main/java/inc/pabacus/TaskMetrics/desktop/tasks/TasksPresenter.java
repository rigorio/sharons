package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.project.ProjectFXAdapter;
import inc.pabacus.TaskMetrics.api.project.ProjectHandler;
import inc.pabacus.TaskMetrics.api.project.ProjectService;
import inc.pabacus.TaskMetrics.api.tasks.TaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.TaskWebRepository;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.desktop.edit.EditView;
import inc.pabacus.TaskMetrics.desktop.edit.EditableTaskHolder;
import inc.pabacus.TaskMetrics.desktop.newTask.NewTaskView;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTask;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackerView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
  private TableView<ProjectFXAdapter> taskTimesheet;

  @FXML
  private JFXButton refreshButton;
  @FXML
  private JFXButton startButton;

  @FXML
  private TableView<XpmTaskAdapter> tasksTable;

  private TaskHandler taskHandler;

  private ProjectService projectService;
  private ActivityHandler activityHandler;
  private XpmTaskWebHandler xpmTaskHandler;

  public TasksPresenter() {
    taskHandler = new TaskHandler(new TaskWebRepository());
    projectService = new ProjectHandler();
    activityHandler = new ActivityHandler();
    xpmTaskHandler = new XpmTaskWebHandler();
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

//    initTaskSheet();
//    refreshingService();
    statusBox.getItems().addAll(new ArrayList<String>() {{
      add("In Progress");
      add("Pending");
      add("Done");
      add("All");
    }});

  }

  @FXML
  public void wowStatusBoxChange() {
    String value = statusBox.getValue();
    ObservableList<XpmTaskAdapter> tasksByStatus;
    if (value.equals("All")) {
      tasksByStatus = FXCollections.observableArrayList(getAllTasks());
    } else {
      tasksByStatus = getTasksByStatus(value);
    }
    tasksTable.setItems(tasksByStatus);
  }

  public void editTask() {

    if (EditableTaskHolder.getTask() != null)
      return;


    XpmTaskAdapter selectedItem = tasksTable.getSelectionModel().getSelectedItem();
    EditableTaskHolder.setTask(selectedItem);

    EditView view = new EditView();

    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);
    stage.setScene(new Scene(view.getView()));
    stage.show();

//    GuiManager.getInstance().displayView(view);
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
//    selectedItem.setStartTime(new SimpleStringProperty(formatter.format(LocalTime.now())));
//    startButton.setDisable(true);
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
//    initTaskTimeSheet();

    totalbillable.setText("7.49");
    totalNonBillable.setText("1.92");
    this.totalHours.setText("9.41");
    totalPercentBillable.setText("80%");
    totalInvoice.setText("$491.00");

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

//    initTaskTimeSheet();
  }

  private void refreshingService() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(this::refreshTasks, 5L, 5L, TimeUnit.SECONDS);
  }

  private List<XpmTaskAdapter> getAllTasks() {
    List<XpmTask> allTasks = xpmTaskHandler.findAll();
//    System.out.println("yare yare daze");
//    allTasks.forEach(System.out::println);
    return FXCollections
        .observableArrayList(allTasks.stream()
                                 .map(XpmTaskAdapter::new)
                                 .collect(Collectors.toList()));
  }

  /**
   * Not currently in use, but not yet removed
   * Just in case i might need them for future references
   *
   * @param table table to be modified
   */
  private void hideHeaders(TableView table) {
    table.widthProperty().addListener((observable, oldValue, newValue) -> {
      Pane header = (Pane) table.lookup("TableHeaderRow");
      if (header.isVisible()) {
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
      }
    });
  }

  private void responsiveness(){
    //transition
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(1000)); // 1 second
    fadeTransition.setNode(mainPane);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.play();
  }

}
