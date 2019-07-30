package inc.pabacus.TaskMetrics.api.generateToken;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.api.cacheService.CacheService;
import inc.pabacus.TaskMetrics.api.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.api.user.UserHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AuthenticatorService {

  private static final Logger logger = Logger.getLogger(StandupService.class);
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private static String HOST;
  private final HostConfig hostConfig;
  private ScheduledFuture<?> scheduledFuture;
  private static final long DEFAULT_INTERVAL = 3000000; // 55 minutes
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();

  private UserHandler userHandler;
  private CacheService<CacheKey, String> cacheService;

  public AuthenticatorService() {
    hostConfig = new HostConfig();
    HOST = hostConfig.getHost();
    userHandler = BeanManager.userHandler();
    cacheService = new StringCacheService();
  }

  public boolean authenticateHrisAccount(Credentials credentials) {
    try {
      AuthenticateEntity authenticateEntity = AuthenticateEntity.builder()
          .userNameOrEmailAddress(credentials.getUsername())
          .password(credentials.getPassword())
          .rememberClient(true)
          .build();
      String s = mapper.writeValueAsString(authenticateEntity);
      RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
      Call call = client.newCall(new Request.Builder()
                                     .url(hostConfig.getHris() + "/api/tokenauth/authenticate")
                                     .post(requestBody)
                                     .build());
      String string = call.execute().body().string();
      Map<String, Object> map = mapper.readValue(string,
                                                 new TypeReference<Map<String, Object>>() {});
      boolean success = (boolean) map.get("success");
      if (!success)
        return false;
      Map<String, String> result = (Map<String, String>) map.get("result");
      String accessToken = result.get("accessToken");
      cacheService.put(CacheKey.SHRIS_TOKEN, "bearer " + accessToken);
      retrieveEmployeeId();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean authenticateTribelyAccount(Credentials credentials) {
    try {

      String jsonString = mapper.writeValueAsString(credentials);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/token")
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      System.out.println(responseString);
//      Object object = mapper.readValue(responseString, new TypeReference<Object>() {});
      if (responseString.contains("Bad Request") || responseString.length() < 10) {
        return false;
      }
      cacheService.put(CacheKey.TRIBELY_TOKEN, responseString);
      TokenRepository.setToken(new Token(responseString));
//      return credentials;
    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e.getMessage());
//      return credentials;
    }
    return true;
  }

  public void invalidCredentials() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText("Invalid username or password");
    alert.showAndWait();
  }

  public void retrieveEmployeeId() {
    try {
      String accessToken = cacheService.get(CacheKey.SHRIS_TOKEN);
      Call call = client.newCall(new Request.Builder()
                                     .url(hostConfig.getHris() + "/api/services/app/User/GetCurrentUserEmployeeId")
                                     .addHeader("Authorization", accessToken)
                                     .build());
      String responseString = call.execute().body().string();
      Map<String, Object> response = mapper.readValue(responseString,
                                                      new TypeReference<Map<String, Object>>() {});
      String employeeId = response.get("result").toString();
      cacheService.put(CacheKey.EMPLOYEE_ID, employeeId);

      // code below is result of how bad staging api is nothing was working this was just a quick temporary workaround for me
//      Call c = client.newCall(new Request.Builder()
//                                  .url("https://hureyweb-staging.azurewebsites.net/api/services/app/EmployeeTimeLog/GetAllNotDeletedByEmployeeIdAndDate?employeeId=f6befdfe-8876-4b6a-f503-08d704e3effb&logDate=2019-07-24")
//                                  .addHeader("Authorization", accessToken)
//                                  .build()
//      );
//      String string = c.execute().body().string();
//      Map<String, Object> r = mapper.readValue(string,
//                                                      new TypeReference<Map<String, Object>>() {});
//      Map<String, Object> result = (Map<String, Object>) r.get("result");
//      List<Map<String, Object>> items = (List<Map<String, Object>>) result.get("items");
//      Map<String, Object> stringObjectMap = items.get(0);
//      Object employeeId1 = stringObjectMap.get("employeeId");
//      cacheService.put(CacheKey.EMPLOYEE_ID, employeeId1.toString());


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void refreshToken() {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable command = () -> Platform.runLater(() -> {
      String username = userHandler.getUsername();
      String password = userHandler.getPassword();
      authenticateTribelyAccount(new Credentials(username, password));
    });

    scheduledFuture = executor.scheduleAtFixedRate(command, 0, DEFAULT_INTERVAL, TimeUnit.MILLISECONDS);
  }

  public void stopToken() {
    scheduledFuture.cancel(true);
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  private static class AuthenticateEntity {
    private String userNameOrEmailAddress;
    private String password;
    private Boolean rememberClient;
  }
}
