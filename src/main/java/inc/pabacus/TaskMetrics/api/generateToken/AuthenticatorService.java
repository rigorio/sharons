package inc.pabacus.TaskMetrics.api.generateToken;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class AuthenticatorService {

  private static final Logger logger = Logger.getLogger(StandupService.class);
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private static String HOST;
  private final HostConfig hostConfig;
  private ScheduledFuture<?> scheduledFuture;
  private static final long DEFAULT_INTERVAL = 3000000; // 55 minutes
  private OkHttpClient client;
  private ObjectMapper mapper = new ObjectMapper();
  private StringCacheService cacheService;

  public AuthenticatorService() {
    client = SslUtil.getSslOkHttpClient();
    hostConfig = new HostConfig();
    HOST = hostConfig.getHost();
    cacheService = new StringCacheService();
  }

  public ReqEnt retrieveRequestItems(Credentials credentials) {
    try {
      String requestString = mapper.writeValueAsString(credentials);
      RequestBody requestBody = RequestBody.create(JSON, requestString);

      Call call = client.newCall(new Request.Builder()
                                     .url(hostConfig.getHost() + "/api/token/login")
                                     .post(requestBody)
                                     .build());

      Response response = call.execute();
      if (response.code() >= 400 && response.code() <= 500) {
        return returnError(response.body().string());
      } else if (response.code() >= 500) {
        return returnError("There was a problem with the server.");
      }
      String responseString = response.body().string();
      ReqEnt reqEnt = mapper.readValue(responseString, new TypeReference<ReqEnt>() {});
      reqEnt.setSuccessful(true);
      return reqEnt;
    } catch (Exception e) {
      e.printStackTrace();
      return returnError(e.toString());
    }
  }

  public ReqEnt retrieveHureyItems(String un, String pw) {
    try {
      String requestString = mapper.writeValueAsString(new HashMap<String, Object>() {{
        put("userNameOrEmailAddress", un);
        put("password", pw);
        put("rememberClient", true);
      }});
      RequestBody requestBody = RequestBody.create(JSON, requestString);

      String host = cacheService.get(CacheKey.HUREY_HOST);
      Call call = client.newCall(new Request.Builder()
                                     .url(host + "/api/tokenauth/authenticate")
                                     .post(requestBody)
                                     .build());
      String string = call.execute().body().string();
      System.out.println(string);
      HureyResponse response = mapper.readValue(string, new TypeReference<HureyResponse>() {});

      if (!response.getSuccess())
        return returnError(response.getError().toString());

      HureyResult result = mapper.readValue(mapper.writeValueAsString(response.getResult()), new TypeReference<HureyResult>() {});
      String accessToken = "bearer " + result.getAccessToken();
      // -----------------------------------------------
      String employeeIdResponse = client
          .newCall(new Request.Builder()
                       .url(host + "/api/services/app/User/GetCurrentUserEmployeeId")
                       .addHeader("Authorization", accessToken)
                       .build())
          .execute()
          .body()
          .string();

      HureyResponse hureyResponse = mapper.readValue(employeeIdResponse, new TypeReference<HureyResponse>() {});

      if (!hureyResponse.getSuccess())
        return returnError(hureyResponse.getError().toString());

      String employeeId = hureyResponse.getResult().toString();

      return new ReqEnt.ReqEntBuilder()
          .successful(true)
          .hureyToken(accessToken)
          .employeeId(employeeId)
          .build();
    } catch (Exception e) {
      e.printStackTrace();
      return returnError(e.toString());
    }
  }

  public void retrieveEmployeeManagerId() {
    StringCacheService stringCacheService = new StringCacheService();
    String employeeId = stringCacheService.get(CacheKey.EMPLOYEE_ID);
    try {
      String accessToken = stringCacheService.get(CacheKey.SHRIS_TOKEN);
      Call call = client.newCall(new Request.Builder()
                                     .url(cacheService.get(CacheKey.HUREY_HOST) + "/api/services/app/Employee/Get?id=" + employeeId)
                                     .addHeader("Authorization", accessToken)
                                     .build());
      String responseString = call.execute().body().string();
      Map<String, Object> response = mapper.readValue(responseString,
                                                      new TypeReference<Map<String, Object>>() {});

      Map<String, Object> result = (Map<String, Object>) response.get("result");
      Object managerEmployeeId = result.get("managerEmployeeId");

      stringCacheService.put(CacheKey.MANAGER_ID, managerEmployeeId.toString());

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

  private ReqEnt returnError(String string) {
    return ReqEnt.builder()
        .successful(false)
        .error(string)
        .build();
  }

  public void stopToken() {
    scheduledFuture.cancel(true);
  }


}
