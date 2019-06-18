package inc.pabacus.TaskMetrics.desktop.support;

import inc.pabacus.TaskMetrics.api.leave.LeaveService;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class SupportPresenter implements Initializable {

  @FXML
  private TableView leaveTable;

  private LeaveService leaveService;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    leaveService = BeanManager.leaveService();
  }
}
