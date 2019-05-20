package inc.pabacus.TaskMetrics.api.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityHandler {

  private ActivityWebRepository repository;

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");


  public ActivityHandler() {
    repository = new ActivityWebRepository();
  }

  public void saveActivity(Activity activity) {
    saveActivity(activity.getActivity());
  }

  public void saveActivity(UserActivity userActivity) {
    try {
      saveUserActivity(userActivity);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveActivity(String activity) {
    try {
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
      UserActivity userActivity = UserActivity.builder()
          .activity(activity)
          .date(LocalDate.now().toString())
          .startTime(timeFormatter.format(LocalTime.now()))
          .build();


      saveUserActivity(userActivity);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void saveUserActivity(UserActivity userActivity) throws IOException {
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
  }
}
