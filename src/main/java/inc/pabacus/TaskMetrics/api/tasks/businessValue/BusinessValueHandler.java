package inc.pabacus.TaskMetrics.api.tasks.businessValue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusinessValueHandler {

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public List<BusinessValue> getAll() {
    Request request = new Request.Builder()
        .url("http://localhost:8080/api/businessValue")
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", TokenRepository.getToken().getToken())
        .method("GET", null)
        .build();
    List<BusinessValue> businessValues = new ArrayList<>();
    try {
      String jsonString = client.newCall(request).execute().body().string();
      businessValues = mapper.readValue(jsonString, new TypeReference<List<BusinessValue>>() {});

    } catch (IOException e) {
      e.printStackTrace();
    }
    return businessValues;
  }

}
