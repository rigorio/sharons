package inc.pabacus.TaskMetrics.desktop.taskTimesheet;

import com.jfoenix.controls.JFXDatePicker;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.TaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.TaskConnector;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TaskTimesheetPresenter implements Initializable {

  @FXML
  private JFXDatePicker datePicker;
  @FXML
  private AnchorPane mainPane;
  @FXML
  private TableView<TaskAdapter> taskTimesheet;
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
  private TaskConnector taskConnector;

  public TaskTimesheetPresenter() {
    taskConnector = new TaskConnector();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initTaskSheet();
  }

  @FXML
  public void filterSummaries() {
    String date = datePicker.getValue().toString();
    System.out.println("date is " + date);
    filterTaskTimesheet(date);
  }

  @FXML
  public void viewYesterday() {
    filterTaskTimesheet(LocalDate.now().minus(1, ChronoUnit.DAYS).toString());
  }

  @FXML
  public void viewToday() {
    filterTaskTimesheet(LocalDate.now().toString());
  }

  @FXML
  public void viewAll() {
    initTaskTimeSheet();
  }

  private void initTaskSheet() {

    TableColumn<TaskAdapter, String> project = new TableColumn<>("Job");
    project.setCellValueFactory(param -> {
      // handle null value
      String getProject;
      try {
        getProject = param.getValue().getJob().getValue();
      } catch (Exception e) {
        getProject = "";
      }
      return new SimpleStringProperty("" + getProject);
    });

    TableColumn<TaskAdapter, String> dateCreated = new TableColumn<>("Date Created");
    dateCreated.setCellValueFactory(param -> param.getValue().getDateCreated());

    TableColumn<TaskAdapter, String> dateCompleted = new TableColumn<>("Date Finished");
    dateCompleted.setCellValueFactory(param -> param.getValue().getDateFinished());

    TableColumn<TaskAdapter, String> startTime = new TableColumn<>("Start Time");
    startTime.setCellValueFactory(param -> {
      // handle null value
      String getStartTime;
      try {
        getStartTime = param.getValue().getStartTime().getValue();
      } catch (Exception e) {
        getStartTime = "";
      }
      return new SimpleStringProperty("" + getStartTime);
    });

    TableColumn<TaskAdapter, String> endTime = new TableColumn<>("End Time");
    endTime.setCellValueFactory(param -> {
      // handle null value
      String getEndTime;
      try {
        getEndTime = param.getValue().getEndTime().getValue();
      } catch (Exception e) {
        getEndTime = "";
      }
      return new SimpleStringProperty("" + getEndTime);
    });

    TableColumn<TaskAdapter, String> billable = new TableColumn<>("Billable?");
    billable.setCellValueFactory(param -> {
      // handle null value
      Boolean isBillable;
      try {
        isBillable = param.getValue().getBillable().getValue().equals("Y");
      } catch (Exception e) {
        isBillable = false;
      }

      return new SimpleStringProperty("" + (isBillable ? "Y" : "N"));
    });

    TableColumn<TaskAdapter, String> billableHours = new TableColumn<>("Hours Spent");
    billableHours.setCellValueFactory(param -> {
      TaskAdapter taskAdapter = param.getValue();
      // handle null value
      String totalTimeSpent;
      try {
        totalTimeSpent = taskAdapter.getTotalTimeSpent().get();
      } catch (Exception e) {
        totalTimeSpent = "";
      }

//    Boolean isBillable = taskAdapter.getBillable().getValue();
      return new SimpleStringProperty("" + totalTimeSpent);
    });

    TableColumn<TaskAdapter, String> percentCompleted = new TableColumn<>("Percentage Completed");
    percentCompleted.setCellValueFactory(param -> {
      // handle null value
      String getPercentage;
      try {
        getPercentage = param.getValue().getPercentCompleted().getValue();
      } catch (Exception e) {
        getPercentage = "0%";
      }
      return new SimpleStringProperty("" + getPercentage);
    });

    TableColumn<TaskAdapter, String> task = new TableColumn<>("Task");
    task.setCellValueFactory(param -> {
      // handle null value
      String getTask;
      try {
        getTask = param.getValue().getTask().getValue();
      } catch (Exception e) {
        getTask = "";
      }
      return new SimpleStringProperty("" + getTask);
    });


    TableColumn<TaskAdapter, String> description = new TableColumn<>("Description");
    description.setCellValueFactory(param -> {
      // handle null value
      String getDescription;
      try {
        getDescription = param.getValue().getDescription().getValue();
      } catch (Exception e) {
        getDescription = "";
      }
      return new SimpleStringProperty("" + getDescription);
    });


    taskTimesheet.getColumns().addAll(project, dateCreated, dateCompleted, startTime, endTime, billable,
                                      billableHours, percentCompleted, task, description);
    initTaskTimeSheet();

    totalbillable.setText("7.49");
    totalNonBillable.setText("1.92");
    this.totalHours.setText("9.41");
    totalPercentBillable.setText("80%");
    totalInvoice.setText("$491.00");

  }

  private void initTaskTimeSheet() {
    taskTimesheet.setItems(getXpmTimesheet(allTimesheets()));
  }

  private void filterTaskTimesheet(String date) {
    List<Task> timesheets = allTimesheets()
        .stream()
        .filter(timesheet -> {
          String dateFinished = timesheet.getDateFinished();
          return timesheet.getDateCreated().equals(date) || (dateFinished != null && dateFinished.equals(date));
        })
        .collect(Collectors.toList());
    taskTimesheet.setItems(getXpmTimesheet(timesheets));
  }

  private List<Task> allTimesheets() {
    return taskConnector.findAll();
  }

  private ObservableList<TaskAdapter> getXpmTimesheet(List<Task> timesheets) {
    List<TaskAdapter> tasks = timesheets
        .stream()
        .filter(xpmTask -> xpmTask.getStatus().equals(Status.IN_PROGRESS.getStatus()) || xpmTask.getStatus().equals(Status.DONE.getStatus()))
        .map(TaskAdapter::new)
        .collect(Collectors.toList());
    return FXCollections.observableArrayList(tasks);
  }
}
