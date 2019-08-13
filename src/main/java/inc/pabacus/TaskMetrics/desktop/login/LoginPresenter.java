package inc.pabacus.TaskMetrics.desktop.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.api.generateToken.AuthenticatorService;
import inc.pabacus.TaskMetrics.api.generateToken.Credentials;
import inc.pabacus.TaskMetrics.api.generateToken.ReqEnt;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.dashboard.DashboardView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

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
  private AuthenticatorService service;
  private UserHandler userHandler;
  private ActivityHandler activityHandler;
  private StringCacheService cacheService;

  public LoginPresenter() {
    kickerService = BeanManager.kickerService();
    service = BeanManager.tokenService();
    userHandler = BeanManager.userHandler();
    activityHandler = new ActivityHandler();
    cacheService = new StringCacheService();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
//    usernameField.setText("userName");
//    passwordField.setText("password");
//    login();
  }

  @FXML
  public void login() {
    if (blankFields()) return;
    //for smooth loading
    mainPane.getScene().setCursor(Cursor.WAIT);
//    PauseTransition pause = new PauseTransition(Duration.millis(500)); //half second
//    pause.setOnFinished(event -> {

    String userName = this.usernameField.getText();
    String passWord = passwordField.getText();
    boolean isSuccessfullyAuthenticated = checkCredentials(userName, passWord);
    if (isSuccessfullyAuthenticated) {
      saveCredentials();
//      kickerService.kicker();
      activityHandler.saveTimestamp(Activity.ONLINE);
      GuiManager.getInstance().changeView(new DashboardView());
//    });
//    pause.play();
    }
  }

  private boolean blankFields() {
    if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
      alertError("Invalid", "Please fill out all the fields");
      return true;
    }
    return false;
  }

  private boolean checkCredentials(String userNameText, String passWordText) {
    Credentials credentials = new Credentials(userNameText, passWordText);
    ReqEnt reqEnt = service.retrieveRequestItems(credentials);
    if (!reqEnt.isSuccessful()) {
      alertError("Unsuccessful attempt", reqEnt.getError());
      return false;
    }
    cacheService.put(CacheKey.TRIBELY_TOKEN, reqEnt.getTribelyToken());
    cacheService.put(CacheKey.SHRIS_TOKEN, reqEnt.getHureyToken());
    cacheService.put(CacheKey.EMPLOYEE_ID, reqEnt.getEmployeeId());
    service.retrieveEmployeeManagerId();
    return true;
  }

  private void alertError(String s, String error) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(s);
    alert.setHeaderText(null);
    alert.setContentText(error);
    alert.showAndWait();
  }

  private void saveCredentials() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    userHandler.setUsername(username);
    userHandler.setPassword(password);
  }

}
