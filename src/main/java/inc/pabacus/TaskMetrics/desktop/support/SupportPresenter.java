package inc.pabacus.TaskMetrics.desktop.support;

import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.leave.Leave;
import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.leaveViewer.LeaveHolder;
import inc.pabacus.TaskMetrics.desktop.leaveViewer.LeaveViewerView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
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
  private JFXComboBox statusComboBox;
  @FXML
  private TableView<LeaveAdapter> leaveTable;

  private LeaveService leaveService;
  private UserHandler userHandler;
  private TemporaryStaffHolder staffHolder;

  public SupportPresenter() {
    leaveService = BeanManager.leaveService();
    userHandler = BeanManager.userHandler();
    staffHolder = new TemporaryStaffHolder();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    initTable();

  }

  private void initTable() {
    ObservableList<LeaveAdapter> allLeaves = getAndConvertLeaves();

    TableColumn<LeaveAdapter, String> staff = new TableColumn<>("Staff");
    staff.setCellValueFactory(param -> {
      Long value = param.getValue().getUserId().getValue();
      return new SimpleStringProperty(staffHolder.getStaff(value));
    });

    TableColumn<LeaveAdapter, String> startDate = new TableColumn<>("Start Date");
    startDate.setCellValueFactory(param -> param.getValue().getStartDate());

    TableColumn<LeaveAdapter, String> endDate = new TableColumn<>("End Date");
    endDate.setCellValueFactory(param -> param.getValue().getEndDate());

    TableColumn<LeaveAdapter, String> status = new TableColumn<>("Status");
    status.setCellValueFactory(param -> param.getValue().getStatus());

    TableColumn<LeaveAdapter, String> typeOfRequest = new TableColumn<>("Type");
    typeOfRequest.setCellValueFactory(param -> param.getValue().getTypeOfRequest());

    leaveTable.getColumns().addAll(staff, startDate, endDate,
                                   typeOfRequest, status);
    leaveTable.setItems(allLeaves);
  }

  @FXML
  public void modifyLeave() {
    LeaveAdapter leave = leaveTable.getSelectionModel().getSelectedItem();
    LeaveHolder.setLeave(new Leave(leave));
    GuiManager.getInstance().displayView(new LeaveViewerView());
  }

  private ObservableList<LeaveAdapter> getAndConvertLeaves() {
    return FXCollections.observableArrayList(getLeaves().stream()
                                                 .map(LeaveAdapter::new)
                                                 .collect(Collectors.toList()));
  }

  private List<Leave> getLeaves() {
//    List<Leave> all = leaveService.getAll();
    return getApprovalQueue();
  }


  private List<Leave> getApprovalQueue() {
    List<Leave> all = leaveService.getAllLeaves();
    String username = userHandler.getUsername();
    return all.stream()
        .filter(leave -> leave.getApprovers().stream()
            .anyMatch(approver -> approver.getApprover().equals(username)))
        .collect(Collectors.toList());
  }
}
