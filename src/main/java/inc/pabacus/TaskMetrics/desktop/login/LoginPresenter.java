package inc.pabacus.TaskMetrics.desktop.login;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.generateToken.Credentials;
import inc.pabacus.TaskMetrics.api.generateToken.TokenService;
import inc.pabacus.TaskMetrics.api.kicker.KickStatus;
import inc.pabacus.TaskMetrics.api.kicker.KickerService;
import inc.pabacus.TaskMetrics.api.kicker.TokenHolder;
import inc.pabacus.TaskMetrics.api.user.Password;
import inc.pabacus.TaskMetrics.api.user.UserRepository;
import inc.pabacus.TaskMetrics.api.user.Username;
import inc.pabacus.TaskMetrics.desktop.dashboard.DashboardView;
import inc.pabacus.TaskMetrics.desktop.kickout.KickoutView;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import okhttp3.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPresenter implements Initializable {

  @FXML
  private JFXTextField usernameField;

  @FXML
  private JFXPasswordField passwordField;

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private KickerService kickerService = BeanManager.kickerService();
  private TokenService service = new TokenService();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }

  @FXML
  public void login() {
    try {
      if (blankFields()) return;
      String userName = this.usernameField.getText();
      String passWord = passwordField.getText();
      jwtLogin(userName,passWord);
      createCredential();
      service.refreshToken();
      String response = kickerService.login(userName);
      KickStatus status = mapper.readValue(response, new TypeReference<KickStatus>() {});
      TokenHolder.setToken(status.getNewToken());
      if (status.getStatus().equals("Exists")) {
        kickerService.setUsername(userName);
        kickerService.setOldToken(status.getOldToken());
        GuiManager.getInstance().displayView(new KickoutView());
      }
      kickerService.kicker();
      GuiManager.getInstance().changeView(new DashboardView());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void jwtLogin(String userNameText,String passWordText) {

    Credentials credentials = service.generateToken(new Credentials(userNameText,passWordText));
    System.out.println(credentials); // for checking
  }

  private boolean blankFields() {
    if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Invalid");
      alert.setHeaderText(null);
      alert.setContentText("Please fill out all the fields");
      alert.showAndWait();
      return true;
    }
    return false;
  }

  private void createCredential(){
    String username = usernameField.getText();
    String password = passwordField.getText();
    UserRepository.setUsername(new Username(username));
    UserRepository.setPassword(new Password(password));
  }
}
