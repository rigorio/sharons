package inc.pabacus.TaskMetrics.api.timesheet.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.activity.ActivityTimestamp;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogWebRepository;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidationHandler {

  private static final Logger logger = Logger.getLogger(DailyLogWebRepository.class);
  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private ValidationService validationService = new ValidationService();

  public List<ActivityTimestamp> save(List<ActivityTimestamp> userActivities) {

    List<ActivityTimestamp> unrecognizedActivities = new ArrayList<>();
    try {

      String jsonString = mapper.writeValueAsString(userActivities);
      RequestBody body = RequestBody.create(JSON, jsonString);

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/activities/unrecognized")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(body)
                                     .build());
      String responseString = call.execute().body().string();
      unrecognizedActivities = mapper.readValue(responseString, new TypeReference<List<ActivityTimestamp>>() {});
    } catch (IOException e) {
      e.printStackTrace();
    }
    return unrecognizedActivities;
  }

  public void runValidationChecks() {
    List<ActivityTimestamp> unrecognizedLogs = validationService.unrecognizedLogs();
    List<ActivityTimestamp> unrecognizedTasks = validationService.unrecognizedTasks();
    save(unrecognizedLogs);
    save(unrecognizedTasks);
  }

}
