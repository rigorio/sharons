package inc.pabacus.TaskMetrics.desktop.taskTimesheet;

import inc.pabacus.TaskMetrics.api.project.ProjectFXAdapter;
import inc.pabacus.TaskMetrics.api.project.ProjectHandler;
import inc.pabacus.TaskMetrics.api.project.ProjectService;
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
  private TableView<ProjectFXAdapter> taskTimesheet;
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
  private ProjectService projectService;

  public TaskTimesheetPresenter(){
    projectService = new ProjectHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initTaskSheet();
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

}
