package inc.pabacus.TaskMetrics.api.generateToken;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TokenService {

  private static final Logger logger = Logger.getLogger(StandupService.class);
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private static String HOST;
  private ScheduledFuture<?> scheduledFuture;
  private static final long DEFAULT_INTERVAL = 3000000; // 55 minutes

  private UserHandler userHandler;

  public TokenService() {
    HOST = new HostConfig().getHost();
    userHandler = BeanManager.userHandler();
  }

  public void generateToken(Credentials credentials) {
    try {
      OkHttpClient client = new OkHttpClient();
      ObjectMapper mapper = new ObjectMapper();

      String jsonString = mapper.writeValueAsString(credentials);
      RequestBody body = RequestBody.create(JSON, jsonString);
      System.out.println(HOST);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/token")
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      System.out.println(responseString);
//      Object object = mapper.readValue(responseString, new TypeReference<Object>() {});
      if (responseString.contains("Bad Request")) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid username or password");
        alert.showAndWait();
        return;
      }
      TokenRepository.setToken(new Token(responseString));
//      return credentials;
    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e.getMessage());
//      return credentials;
    }
  }

  public void refreshToken() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable command = () -> Platform.runLater(() -> {
      String username = userHandler.getUsername();
      String password = userHandler.getPassword();
      TokenService service = new TokenService();
      service.generateToken(new Credentials(username, password));
    });

    scheduledFuture = executor.scheduleAtFixedRate(command, 0, DEFAULT_INTERVAL, TimeUnit.MILLISECONDS);
  }

  public void stopToken() {
    scheduledFuture.cancel(true);
  }
}
