package inc.pabacus.TaskMetrics.api.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityHandler {

  private static final Logger logger = Logger.getLogger(ActivityHandler.class);
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();

  public ActivityHandler() {
  }

  public void saveActivity(Activity activity) {
    saveActivity(activity.getActivity());
  }

  public void saveActivity(UserActivity userActivity) {
    try {
      sendToHost(userActivity);
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
  }

  public void saveActivity(String activity) {
    try {
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
      UserActivity userActivity = UserActivity.builder()
          .activity(activity)
          .date(LocalDate.now().toString())
          .time(timeFormatter.format(LocalTime.now()))
          .build();


      sendToHost(userActivity);
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }

  }

  private void sendToHost(UserActivity userActivity) throws IOException {
    List<UserActivity> ua = new ArrayList<>();
    ua.add(userActivity);
    String jsonValue = mapper.writeValueAsString(ua);
    RequestBody requestBody = RequestBody.create(JSON, jsonValue);

    Call call = client.newCall(new Request.Builder()
                                   .url(HOST + "/api/activities")
                                   .addHeader("Authorization", TokenRepository.getToken().getToken())
                                   .post(requestBody)
                                   .build());
    ResponseBody body = call.execute().body();
    String jsonString = body.string();
    System.out.println(jsonString);
  }
}
