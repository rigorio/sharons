package inc.pabacus.TaskMetrics.api.standuply;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.desktop.standuply.StanduplyView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Platform;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StandupService {

  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private static final String HOST = "http://localhost:8080";
  private ScheduledFuture<?> scheduledFuture;

  public void runStandup() {
    // amm what. harder than i thought.. i will hack. **hacking noise**
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable command = () -> Platform.runLater(() -> {

      if (isStandupTime()) {
        GuiManager.getInstance().displayView(new StanduplyView());
        scheduledFuture.cancel(true);
      }
    });

    scheduledFuture = executor.scheduleAtFixedRate(command, 0, 1, TimeUnit.SECONDS);

  }

  public void close() {
    scheduledFuture.cancel(true);
  }

  public StandupAnswer sendAnswer(StandupAnswer answer) {
    StandupAnswer standupAnswer;
    try {
      OkHttpClient client = new OkHttpClient();
      ObjectMapper mapper = new ObjectMapper();

      String jsonString = mapper.writeValueAsString(answer);
      RequestBody body = RequestBody.create(JSON, jsonString);

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/repositories") // TODO add path
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      standupAnswer = mapper.readValue(responseBody.string(),
                                       new TypeReference<StandupAnswer>() {});
      return standupAnswer;
    } catch (IOException e) {
      e.printStackTrace();
      return answer;
    }
  }

  private boolean isStandupTime() {
    OkHttpClient client = new OkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/admin")
        .addHeader("Accept", "application/json")
        .method("GET", null)
        .build();

    Response response = null;

    try {

      response = client.newCall(request).execute();
      String getTimes = null;
      getTimes = response.body().string();
      String getTimeJson = getTimes.replaceAll("\\[|\\]", "");
      JSONObject json = new JSONObject(getTimeJson);
      String syncresponse = json.getString("time");
      String hour;
      String minute;

      if (syncresponse.contains(":")) {
        String[] parts = syncresponse.split(":");
        hour = parts[0];
        minute = parts[1];
      } else {
        hour = syncresponse;
        minute = "00";
      }

      LocalTime now = LocalTime.now();
      LocalTime schedule = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
      return now.getHour() == schedule.getHour() && now.getMinute() == schedule.getMinute();

    } catch (IOException | JSONException e) {
      return false;
    }

  }
}
