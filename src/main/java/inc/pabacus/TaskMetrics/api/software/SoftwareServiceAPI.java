package inc.pabacus.TaskMetrics.api.software;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.utils.web.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import javafx.application.Platform;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class SoftwareServiceAPI {
  private static String HOST;
  private HostConfig hostConfig = new HostConfig();
  private SoftwareService softwareService;
  private ScheduledFuture<?> scheduledFuture;

  public SoftwareServiceAPI() {
    HOST = hostConfig.getHost();
  }

  public void sendSoftwareData() {
//    getSoftwareMonitoringMinutes();
//    String minutes = getSoftwareMonitoringMinutes();

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable task = () -> Platform.runLater(() -> {

      try {

        softwareService = new SoftwareHandler();
        List<SoftwareData> allSoftware = softwareService.getSoftware();
        SoftwareDataEntity entity = new SoftwareDataEntity();
        entity.setRunningSoftwares(allSoftware);
        entity.setTimeStamp(LocalDateTime.now().toString());
        MediaType mediaType = MediaType.parse("application/json");
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(entity);
        RequestBody requestBody = RequestBody.create(mediaType, content);

        OkHttpClient client = SslUtil.getSslOkHttpClient();

        Request request = new Request.Builder()
            .url(HOST + "/api/usersoftwaresapi")
            .addHeader("content-type", "application/json")
            .addHeader("Authorization", new StringCacheService().get(CacheKey.TRIBELY_TOKEN))
            .post(requestBody)
            .build();

        Response response = client.newCall(request).execute();

      } catch (Exception x) {
        x.printStackTrace();
      }

    });
    scheduledFuture = executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);

  }

  public void cancel() {
    scheduledFuture.cancel(true);
  }

  public String getSoftwareMonitoringMinutes() {
    OkHttpClient client = SslUtil.getSslOkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/monitorSoftware")
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", new StringCacheService().get(CacheKey.TRIBELY_TOKEN))
        .method("GET", null)
        .build();

    String getSoftwareMonitoringMinutes = null;

    try {

      Response response = client.newCall(request).execute();
      String getTimes = response.body().string();

      if (getTimes.equals("[]") || getTimes.equals("")) {
        // default value of 5 minutes
        getSoftwareMonitoringMinutes = "5";
      } else {
        String getTimeJson = getTimes.replaceAll("\\[|\\]", "");
        JSONObject json = new JSONObject(getTimeJson);
        getSoftwareMonitoringMinutes = json.getString("minutes");
      }


    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }
    return getSoftwareMonitoringMinutes;
  }

}