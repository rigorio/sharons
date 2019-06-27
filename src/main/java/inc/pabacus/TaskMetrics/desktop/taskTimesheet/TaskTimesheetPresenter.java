package inc.pabacus.TaskMetrics.desktop.taskTimesheet;

import inc.pabacus.TaskMetrics.api.tasks.XpmTaskAdapter;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.desktop.taskTimesheet.xpmTimesheet.XpmTimesheetHandler;
import inc.pabacus.TaskMetrics.desktop.taskTimesheet.xpmTimesheet.XpmTimesheetService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TaskTimesheetPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private TableView<XpmTaskAdapter> taskTimesheet;
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
  private XpmTimesheetService xpmTimesheetService;
  private XpmTaskWebHandler xpmTaskWebHandler;

  public TaskTimesheetPresenter() {
    xpmTimesheetService = new XpmTimesheetHandler();
    xpmTaskWebHandler = new XpmTaskWebHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initTaskSheet();
  }

  private void initTaskSheet() {

    TableColumn<XpmTaskAdapter, String> project = new TableColumn<>("Job");
    project.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getJob().get()));

    TableColumn<XpmTaskAdapter, String> startTime = new TableColumn<>("Start Time");
    startTime.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getStartTime().get()));

    TableColumn<XpmTaskAdapter, String> endTime = new TableColumn<>("End Time");
    endTime.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getEndTime().get()));

    TableColumn<XpmTaskAdapter, String> billable = new TableColumn<>("Billable?");
    billable.setCellValueFactory(param -> {
      Boolean isBillable = param.getValue().getBillable().getValue();
      return new SimpleStringProperty("" + (isBillable ? "Y" : "N"));
    });

    TableColumn<XpmTaskAdapter, String> billableHours = new TableColumn<>("Hours Spent");
    billableHours.setCellValueFactory(param -> {
      XpmTaskAdapter xpmTaskAdapter = param.getValue();
      String totalTimeSpent = xpmTaskAdapter.getTotalTimeSpent().get();
      Boolean isBillable = xpmTaskAdapter.getBillable().getValue();
      return new SimpleStringProperty("" + totalTimeSpent);
    });

    TableColumn<XpmTaskAdapter, String> task = new TableColumn<>("Task");
    task.setCellValueFactory(param -> param.getValue().getTask());


    TableColumn<XpmTaskAdapter, String> description = new TableColumn<>("Description");
    description.setCellValueFactory(param -> param.getValue().getDescription());


    taskTimesheet.getColumns().addAll(project, startTime, endTime, billable,
                                      billableHours, task, description);
    initTaskTimeSheet();

    totalbillable.setText("7.49");
    totalNonBillable.setText("1.92");
    this.totalHours.setText("9.41");
    totalPercentBillable.setText("80%");
    totalInvoice.setText("$491.00");

  }

  private void initTaskTimeSheet() {
    taskTimesheet.setItems(getXpmTimesheet());
  }

  private ObservableList<XpmTaskAdapter> getXpmTimesheet() {
    List<XpmTaskAdapter> tasks = xpmTaskWebHandler.findAll()
        .stream()
        .filter(xpmTask -> xpmTask.getStatus().equals(Status.DONE.getStatus()))
        .map(XpmTaskAdapter::new)
        .collect(Collectors.toList());
    return FXCollections.observableArrayList(tasks);
  }

}
