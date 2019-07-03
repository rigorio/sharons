package inc.pabacus.TaskMetrics.api.timesheet.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogWebRepository;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatusUpdateHandler {


  private static final Logger logger = Logger.getLogger(DailyLogWebRepository.class);
  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";

  private List<StatusUpdate> all() {

    List<StatusUpdate> statusUpdates = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/status/updates")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());

      String jsonString = call.execute().body().string();
      statusUpdates = mapper.readValue(jsonString, new TypeReference<List<StatusUpdate>>() {});
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return statusUpdates;
  }


}
