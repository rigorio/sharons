package inc.pabacus.TaskMetrics.api.tasks.businessValue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusinessValueHandler {

  private static final Logger logger = Logger.getLogger(BusinessValueHandler.class);
  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private HostConfig hostConfig = new HostConfig();

  public BusinessValueHandler() {
    HOST = hostConfig.getHost();
  }

  public List<BusinessValue> getAll() {
    Request request = new Request.Builder()
        .url(new HostConfig().getHost() + "/api/jobs/business")
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", TokenRepository.getToken().getToken())
        .method("GET", null)
        .build();
    List<BusinessValue> businessValues = new ArrayList<>();
    try {
      String jsonString = client.newCall(request).execute().body().string();
      businessValues = mapper.readValue(jsonString, new TypeReference<List<BusinessValue>>() {});

    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return businessValues;
  }

}
