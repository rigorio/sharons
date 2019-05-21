package inc.pabacus.TaskMetrics.desktop.kickout;

import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class KickoutPresenter implements Initializable {

  @FXML
  private Label label;
  private KickerService kickerService;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    kickerService = BeanManager.kickerService();
  }

  public void yes() {
    String oldToken = kickerService.getOldToken();
    kickerService.logout(oldToken);
    Stage stage = (Stage) label.getScene().getWindow();
    stage.close();
  }

  public void no() {
    Stage stage = (Stage) label.getScene().getWindow();
    stage.close();
  }
}
