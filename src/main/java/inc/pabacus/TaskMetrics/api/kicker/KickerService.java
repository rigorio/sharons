package inc.pabacus.TaskMetrics.api.kicker;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KickerService {

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private String username;
  private String oldToken;


  public String login(String username) {
    String response = "";
    Call call = client.newCall(new Request.Builder()
                                   .url(HOST + "/api/kickout/" + username)
                                   .header("Authorization", TokenRepository.getToken().getToken())
                                   .build());
    try {
      response = call.execute().body().string();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return response;
  }

  public void logout(String token) {
    Call call = client.newCall(new Request.Builder()
                                   .url(HOST + "/api/kickout/logout?token=" + token)
                                   .header("Authorization", TokenRepository.getToken().getToken())
                                   .build());
    try {
      call.execute();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setUsername(String userNameText) {
    username = userNameText;
  }

  public String getUsername() {
    return username;
  }

  public String getOldToken() {
    return oldToken;
  }

  public void setOldToken(String oldToken) {
    this.oldToken = oldToken;
  }

  public void kicker() {
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    Runnable runnable = () -> {
      if (!exists()) {
        Platform.runLater(() -> GuiManager.getInstance().changeView(new LoginView()));
      }
    };
    service.scheduleAtFixedRate(runnable, 2L, 5L, TimeUnit.SECONDS);
  }

  private Boolean exists() {
    Boolean exists = false;
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/kickout/exists?token=" + TokenHolder.getToken())
                                     .header("Authorization", TokenRepository.getToken().getToken())
                                     .build());
      String response = call.execute().body().string();
      exists = Boolean.valueOf(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return exists;
  }
}
