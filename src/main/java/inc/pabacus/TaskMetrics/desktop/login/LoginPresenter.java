package inc.pabacus.TaskMetrics.desktop.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.generateToken.Token;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.hardware.HardwareServiceAPI;
import inc.pabacus.TaskMetrics.api.screenshot.ScreenshotServiceImpl;
import inc.pabacus.TaskMetrics.api.software.SoftwareServiceAPI;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.desktop.dashboard.DashboardView;
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
  private JFXTextField userName;

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private StandupService standupService = new StandupService();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }

  @FXML
  public void login() {
    if (userName.getText().equals("")){
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Error");
      alert.setContentText("Please fill out all the fields");
      alert.showAndWait();

    }
    else {

      generateToken();
      new HardwareServiceAPI().sendHardwareData();
      standupService.runStandup();
      new SoftwareServiceAPI().sendSoftwareData();
      new ScreenshotServiceImpl().enableScreenShot();

      GuiManager.getInstance().changeView(new DashboardView());
    }
  }

  public void generateToken(){

    String userNameText = userName.getText();
    String role = "admin";
    try {
      String jsonString = "{\"userName\":\"" + userNameText + "\",\"role\":\"" + role +"\"}";
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/token")
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String getToken = responseBody.string();
      TokenRepository.setToken(new Token(getToken));
      System.out.println(getToken);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
