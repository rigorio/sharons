package inc.pabacus.TaskMetrics.api.activity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import lombok.Data;
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
  private static String HOST;
  private HostConfig hostConfig = new HostConfig();
  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private StringCacheService stringCacheService = new StringCacheService();

  public ActivityHandler() {
    HOST = hostConfig.getHost();

  }


  public List<ActivityTimestamp> allTimestamps() {
    List<ActivityTimestamp> userActivities = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/activities")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      userActivities = mapper.readValue(jsonString,
                                        new TypeReference<List<ActivityTimestamp>>() {});
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userActivities;
  }

  public List<ActivityRecord> allRecords() {
    List<ActivityRecord> activityRecords = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/activity/records")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      activityRecords = mapper.readValue(jsonString,
                                         new TypeReference<List<ActivityRecord>>() {});
    } catch (IOException e) {
      e.printStackTrace();
    }
    return activityRecords;
  }


  public void saveRecord(Record record) {
    ActivityRecord activityRecord = ActivityRecord.builder()
        .date(LocalDate.now().toString())
        .time(timeFormatter.format(LocalTime.now()))
        .duration("" + record.getDuration())
        .type(record.getRecordType().getActivity())
        .activity(record.getActivity())
        .userId(1L)
        .build();

    try {

      String jsonValue = mapper.writeValueAsString(activityRecord);
      RequestBody requestBody = RequestBody.create(JSON, jsonValue);

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/activity/record")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .post(requestBody)
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      System.out.println(jsonString);
    } catch (IOException e) {
      System.out.println("ha");
      logger.warn(e.getMessage());
    }
  }

  public void saveTimestamp(Activity activity) {
    ActivityTimestamp activityTimestamp = ActivityTimestamp.builder()
        .activity(activity.getActivity())
        .date(LocalDate.now().toString())
        .time(timeFormatter.format(LocalTime.now()))
        .build();
    saveTimestamp(activityTimestamp);
  }

  public void saveTimestamp(ActivityTimestamp activityTimestamp) {
    try {
      String jsonValue = mapper.writeValueAsString(new ActivityCreate(activityTimestamp.getDate(), activityTimestamp.getTime(), activityTimestamp.getActivity()));
      RequestBody requestBody = RequestBody.create(JSON, jsonValue);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/activities")
                                     .addHeader("Content-Type", "application/json")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .post(requestBody)
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
//      System.out.println("respo " + jsonString);
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
  }

  public String getLastLog() {
    String activity = null;
    OkHttpClient client = SslUtil.getSslOkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/activities?date=" + LocalDate.now())
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
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
    return activity != null ? activity : "";
  }

  @Data
  private class ActivityCreate {
    private String date;
    private String time;
    private String activity;

    public ActivityCreate(String date, String time, String activity) {
      this.date = date;
      this.time = time;
      this.activity = activity;
    }
  }
}
