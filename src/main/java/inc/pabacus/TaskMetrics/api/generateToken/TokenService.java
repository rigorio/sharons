package inc.pabacus.TaskMetrics.api.generateToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.application.Platform;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TokenService {

  private static final Logger logger = Logger.getLogger(StandupService.class);
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private static final String HOST = "http://localhost:8080";
  private ScheduledFuture<?> scheduledFuture;
  private static final long DEFAULT_INTERVAL = 3000000; // 55 minutes

  private UserHandler userHandler;

  public TokenService() {
    userHandler = BeanManager.userHandler();
  }

  public Credentials generateToken(Credentials credentials) {
    try {
      OkHttpClient client = new OkHttpClient();
      ObjectMapper mapper = new ObjectMapper();

      String jsonString = mapper.writeValueAsString(credentials);
      RequestBody body = RequestBody.create(JSON, jsonString);

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/token")
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String getToken = responseBody.string();
      TokenRepository.setToken(new Token(getToken));
      return credentials;
    } catch (IOException e) {
      logger.warn(e.getMessage());
      return credentials;
    }
  }

  public void refreshToken() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable command = () -> Platform.runLater(() -> {
      String username = userHandler.getUsername();
      String password = userHandler.getPassword();
      TokenService service = new TokenService();
      Credentials token = service.generateToken(new Credentials(username, password));
    });

    scheduledFuture = executor.scheduleAtFixedRate(command, 0, DEFAULT_INTERVAL, TimeUnit.MILLISECONDS);
  }

  public void stopToken() {
    scheduledFuture.cancel(true);
  }
}
