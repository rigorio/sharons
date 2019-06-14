package inc.pabacus.TaskMetrics.desktop.taskTimesheet;

import inc.pabacus.TaskMetrics.desktop.taskTimesheet.xpmTimesheet.XpmTimesheetAdapter;
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

public class TaskTimesheetPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private TableView<XpmTimesheetAdapter> taskTimesheet;
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

  public TaskTimesheetPresenter(){
    xpmTimesheetService = new XpmTimesheetHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initTaskSheet();
  }

  private void initTaskSheet() {
    TableColumn<XpmTimesheetAdapter, String> client = new TableColumn<>("Client");
    client.setCellValueFactory(param -> param.getValue().getClient());

    TableColumn<XpmTimesheetAdapter, String> job = new TableColumn<>("Job");
    job.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getJob().get()));

    TableColumn<XpmTimesheetAdapter, String> task = new TableColumn<>("Task");
    task.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getJob().get()));

    TableColumn<XpmTimesheetAdapter, String> date = new TableColumn<>("Date");
    date.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getDate().get()));

    TableColumn<XpmTimesheetAdapter, String> totalTimeSpent = new TableColumn<>("Total Time Spent");
    totalTimeSpent.setCellValueFactory(param -> new SimpleStringProperty("" + param.getValue().getTotalTimeSpent().get() + "%"));


    taskTimesheet.getColumns().addAll(client, job, task,
                                      date, totalTimeSpent);
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

  private ObservableList<XpmTimesheetAdapter> getXpmTimesheet() {
    List<XpmTimesheetAdapter> projects = xpmTimesheetService.getAllgetXpmTimesheetAdapter();
    return FXCollections.observableArrayList(projects);
  }

}
