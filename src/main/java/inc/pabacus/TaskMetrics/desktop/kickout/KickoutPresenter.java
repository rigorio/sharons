package inc.pabacus.TaskMetrics.desktop.kickout;

import inc.pabacus.TaskMetrics.api.kicker.KickerService;
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
  private Stage stage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    stage = (Stage) label.getScene().getWindow();
    kickerService = new KickerService();
  }

  public void yes() {
    kickerService.logout();
    String username = kickerService.getUsername();
    String loginStatus = kickerService.login(username);
    stage.close();
  }

  public void no() {
    stage.close();
  }
}
