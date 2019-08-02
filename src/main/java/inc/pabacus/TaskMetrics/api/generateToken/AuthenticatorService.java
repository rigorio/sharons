package inc.pabacus.TaskMetrics.api.generateToken;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import okhttp3.*;
import org.apache.log4j.Logger;

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

  public AuthenticatorService() {
    client = SslUtil.getSslOkHttpClient();
    hostConfig = new HostConfig();
    HOST = hostConfig.getHost();
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
