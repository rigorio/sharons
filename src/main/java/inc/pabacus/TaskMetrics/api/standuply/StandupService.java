package inc.pabacus.TaskMetrics.api.standuply;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.desktop.standuply.StanduplyView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import javafx.application.Platform;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StandupService {

  private static final Logger logger = Logger.getLogger(StandupService.class);
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private static String HOST;
  private ScheduledFuture<?> scheduledFuture;
  private HostConfig hostConfig = new HostConfig();

  public StandupService() {
    HOST = hostConfig.getHost();
  }

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
    try {
      OkHttpClient client = new OkHttpClient();
      ObjectMapper mapper = new ObjectMapper();

      String jsonString = mapper.writeValueAsString(answer);
      RequestBody body = RequestBody.create(JSON, jsonString);
      //print expected value
      System.out.println(jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/concierge/answers")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      answer = mapper.readValue(responseBody.string(),
                                new TypeReference<StandupAnswer>() {});
      return answer;
    } catch (IOException e) {
      logger.warn(e.getMessage());
      return answer;
    }
  }

  private boolean isStandupTime() {
    OkHttpClient client = new OkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/concierge/schedule")
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", TokenRepository.getToken().getToken())
        .method("GET", null)
        .build();

    try {

      Response response = client.newCall(request).execute();
      String getbody = response.body().string();
      String time = null;
      String frequency = null;
      String day = null;

      JSONArray jsonarray = new JSONArray(getbody);
      for (int i = 0; i < jsonarray.length(); i++) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
        time = jsonobject.getString("time");
        frequency = jsonobject.getString("frequency");
        day = jsonobject.getString("day");
      }

      String[] getHourAndMinute = time.split(":");
      String hour = getHourAndMinute[0];
      String minute = getHourAndMinute[1];

      LocalTime now = LocalTime.now();
      LocalTime schedule = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));

      if (frequency.equalsIgnoreCase("daily")) {
        return now.getHour() == schedule.getHour() && now.getMinute() == schedule.getMinute();
      } else {
        Calendar cal = Calendar.getInstance();
        int dayDate = cal.get(Calendar.DAY_OF_WEEK);

        day = day.toLowerCase();
        switch (day) {
          case "sunday":
            day = "1";
            break;
          case "monday":
            day = "2";
            break;
          case "tuesday":
            day = "3";
            break;
          case "wednesday":
            day = "4";
            break;
          case "thursday":
            day = "5";
            break;
          case "friday":
            day = "6";
            break;
          case "saturday":
            day = "7";
            break;
        }
//        System.out.println(day);
//        System.out.println(dayDate);
        if (day.equals(String.valueOf(dayDate))) {
          return now.getHour() == schedule.getHour() && now.getMinute() == schedule.getMinute();
        } else {
          return false;
        }
      }

    } catch (IOException | JSONException e) {
      return false;
    }

  }

}
