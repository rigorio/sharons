package inc.pabacus.TaskMetrics.desktop.login;

import inc.pabacus.TaskMetrics.desktop.dashboard.DashboardView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPresenter implements Initializable {
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }

  @FXML
  public void login() {
    GuiManager.getInstance().showView(new DashboardView());
  }
}
