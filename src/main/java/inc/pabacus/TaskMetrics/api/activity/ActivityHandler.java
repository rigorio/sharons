package inc.pabacus.TaskMetrics.api.activity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

  public List<UserActivity> all() {
    List<UserActivity> userActivities = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/activities")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      userActivities = mapper.readValue(jsonString,
                                        new TypeReference<List<UserActivity>>() {});
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userActivities;
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
//    System.out.println(jsonString);
  }

  public String getLastLog() {
    String activity = null;
    OkHttpClient client = new OkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/activities?date=" + LocalDate.now())
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", TokenRepository.getToken().getToken())
        .method("GET", null)
        .build();

    try {
      Response response = client.newCall(request).execute();
      String getChats = response.body().string();
      JSONArray jsonarray = new JSONArray(getChats);
      for (int i = 0; i < jsonarray.length(); ++i) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
        activity = jsonobject.getString("activity");
      }
    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }
    return activity;
  }
}
