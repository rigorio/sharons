package inc.pabacus.TaskMetrics.desktop.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.generateToken.Credentials;
import inc.pabacus.TaskMetrics.api.generateToken.AuthenticatorService;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.desktop.dashboard.DashboardView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.net.URL;
import java.util.Optional;
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

  public LoginPresenter() {
    kickerService = BeanManager.kickerService();
    service = BeanManager.tokenService();
    userHandler = BeanManager.userHandler();
    activityHandler = BeanManager.activityHandler();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
//    usernameField.setText("userName");
//    passwordField.setText("password");
//    login();
  }

  @FXML
  public void login() {
    loginUser();
  }

  private boolean checkCredentials(String userNameText, String passWordText) {

    Credentials credentials = new Credentials(userNameText, passWordText);
    boolean isHrisAuthenticated = service.authenticateHrisAccount(credentials);
    if (!isHrisAuthenticated) {
      service.invalidCredentials();
      return false;
    }
    Pair<String, String> pairValues = showTribelyLogin();
    Credentials tribelyCredentials = new Credentials(pairValues.getKey(),
                                                     pairValues.getValue());
    boolean isTribelyAuthenticated = service.authenticateTribelyAccount(tribelyCredentials);
    if (!isTribelyAuthenticated) {
      service.invalidCredentials();
      return false;
    }
    service.retrieveEmployeeId();
    return true;
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

  private void saveCredentials() {
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

  @FXML
  void onLoginUser() {
    loginUser();
  }

  @FXML
  void onLoginPassword() {
    loginUser();
  }

  private Pair<String, String> showTribelyLogin() {
    // Create the custom dialog.
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    dialog.setTitle("Login");
    dialog.setHeaderText("Please Login with your Tribely Account");

// Set the button types.
    ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField username = new TextField();
    username.setPromptText("Username");
    PasswordField password = new PasswordField();
    password.setPromptText("Password");

    grid.add(new Label("Username:"), 0, 0);
    grid.add(username, 1, 0);
    grid.add(new Label("Password:"), 0, 1);
    grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
    Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
    loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
    username.textProperty().addListener((observable, oldValue, newValue) -> {
      loginButton.setDisable(newValue.trim().isEmpty());
    });

    dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
    Platform.runLater(username::requestFocus);

// Convert the result to a username-password-pair when the login button is clicked.
    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == loginButtonType) {
        return new Pair<>(username.getText(), password.getText());
      }
      return null;
    });

    Optional<Pair<String, String>> result = dialog.showAndWait();
    return result.get();
  }
}
