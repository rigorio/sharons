package inc.pabacus.TaskMetrics.desktop.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.generateToken.Credentials;
import inc.pabacus.TaskMetrics.api.generateToken.TokenService;
import inc.pabacus.TaskMetrics.api.kicker.KickStatus;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.kicker.TokenHolder;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.dashboard.DashboardView;
import inc.pabacus.TaskMetrics.desktop.kickout.KickoutView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private JFXTextField usernameField;
  @FXML
  private JFXPasswordField passwordField;

  private KickerService kickerService;
  private TokenService service;
  private UserHandler userHandler;
  private ActivityHandler activityHandler;

  public LoginPresenter() {
    kickerService = BeanManager.kickerService();
    service = BeanManager.tokenService();
    userHandler = BeanManager.userHandler();
    activityHandler = BeanManager.activityHandler();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
//    usernameField.setText("username");
//    passwordField.setText("password");
//    login();
  }

  @FXML
  public void login() {
    loginUser();
  }

  private void jwtLogin(String userNameText, String passWordText) {

    service.generateToken(new Credentials(userNameText, passWordText));
//    System.out.println(credentials); // for checking
  }

  private boolean blankFields() {
    if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Invalid");
      alert.setHeaderText(null);
      alert.setContentText("Please fill out all the fields");
      alert.showAndWait();
      return true;
    }
    return false;
  }

  private void createCredential() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    userHandler.setUsername(username);
    userHandler.setPassword(password);
  }

  private void loginUser() {
    if (blankFields()) return;
    //for smooth loading
    mainPane.getScene().setCursor(Cursor.WAIT);
//    PauseTransition pause = new PauseTransition(Duration.millis(500)); //half second
//    pause.setOnFinished(event -> {

      String userName = this.usernameField.getText();
      String passWord = passwordField.getText();
      jwtLogin(userName, passWord);
      createCredential();
//      kickerService.kicker();
      activityHandler.saveTimestamp(Activity.ONLINE);
      GuiManager.getInstance().changeView(new DashboardView());

//    });
//    pause.play();
  }

  @FXML
  void onLoginUser() {
    loginUser();
  }

  @FXML
  void onLoginPassword() {
    loginUser();
  }
}
