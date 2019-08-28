package inc.pabacus.TaskMetrics.api.kicker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import inc.pabacus.TaskMetrics.utils.cacheService.LocalCacheHandler;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class KickerService {

  private static final Logger logger = Logger.getLogger(KickerService.class);
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private HostConfig hostConfig = new HostConfig();
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private ScheduledFuture<?> scheduledFuture;
  private String username;
  private String oldToken;
  private Runnable runnable;

  public KickerService() {
    HOST = hostConfig.getHost();
  }

  public KickStatus login(String username) {
    String response;
    KickStatus status = null; // not right
    Call call = client.newCall(new Request.Builder()
                                   .url(HOST + "/api/kickout/" + username)
                                   .header("Authorization", LocalCacheHandler.getTribelyToken())
                                   .build());
    try {
      response = call.execute().body().string();
      status = mapper.readValue(response, new TypeReference<KickStatus>() {});
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }

    return status;
  }

  public void logout(String token) {
    Call call = client.newCall(new Request.Builder()
                                   .url(HOST + "/api/kickout/logout?token=" + token)
                                   .header("Authorization", LocalCacheHandler.getTribelyToken())
                                   .build());
    try {
      call.execute();
    } catch (IOException e) {
      logger.warn(e.getMessage());
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

  public void setRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  public void kicker() {
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    Runnable task = () -> Platform.runLater(() -> {
      if (!exists()) {
        Platform.runLater(runnable);
      }
    });
    scheduledFuture = service.scheduleAtFixedRate(task, 2L, 5L, TimeUnit.SECONDS);
  }

  public void stopKicker() {
    scheduledFuture.cancel(true);
  }

  private Boolean exists() {
    Boolean exists = false;
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/kickout/exists?token=" + TokenHolder.getToken())
                                     .header("Authorization", LocalCacheHandler.getTribelyToken())
                                     .build());
      String response = call.execute().body().string();
      exists = Boolean.valueOf(response);
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return exists;
  }
}
