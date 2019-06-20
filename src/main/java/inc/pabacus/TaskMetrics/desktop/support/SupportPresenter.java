package inc.pabacus.TaskMetrics.desktop.support;

import inc.pabacus.TaskMetrics.api.leave.Leave;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SupportPresenter implements Initializable {

  @FXML
  private TableView<LeaveAdapter> leaveTable;

  private LeaveService leaveService;

  public SupportPresenter() {
    leaveService = BeanManager.leaveService();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    ObservableList<LeaveAdapter> allLeaves = getAndConvertLeaves();

    TableColumn<LeaveAdapter, String> startDate = new TableColumn<>("Start Date");
    startDate.setCellValueFactory(param -> param.getValue().getStartDate());

    TableColumn<LeaveAdapter, String> endDate = new TableColumn<>("End Date");
    endDate.setCellValueFactory(param -> param.getValue().getEndDate());

    TableColumn<LeaveAdapter, String> status = new TableColumn<>("Status");
    status.setCellValueFactory(param -> param.getValue().getStatus());

    TableColumn<LeaveAdapter, String> typeOfRequest = new TableColumn<>("Type");
    typeOfRequest.setCellValueFactory(param -> param.getValue().getTypeOfRequest());

    TableColumn<LeaveAdapter, String> approvers = new TableColumn<>("Approved");
    approvers.setCellValueFactory(param -> {
      List<ApproverAdapter> total = param.getValue().getApprovers();
      int approvedTotal = 0;
      for (ApproverAdapter aa : total) {
        String stat = aa.getStatus().get();
        if (stat != null && stat.equals("Approved"))
          approvedTotal++;
      }
      return new SimpleStringProperty(approvedTotal + "/" + total.size());
    });

    leaveTable.getColumns().addAll(startDate, endDate,
                                   typeOfRequest, approvers, status);
    leaveTable.setItems(allLeaves);

  }

  private ObservableList<LeaveAdapter> getAndConvertLeaves() {
    return FXCollections.observableArrayList(getLeaves().stream()
                                                 .map(LeaveAdapter::new)
                                                 .collect(Collectors.toList()));
  }

  private List<Leave> getLeaves() {
    return leaveService.getAll();
  }


}
