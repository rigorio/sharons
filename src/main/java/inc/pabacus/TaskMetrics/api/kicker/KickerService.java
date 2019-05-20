package inc.pabacus.TaskMetrics.api.kicker;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

public class KickerService {

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private String username;


  public String login(String username) {
    String response = "";
    Call call = client.newCall(new Request.Builder()
                                   .url(HOST + "/api/kickout/" + username)
                                   .header("Authorization", TokenRepository.getToken().getToken())
                                   .build());
    try {
      response = call.execute().body().string();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return response;
  }

  public void logout() {
    Call call = client.newCall(new Request.Builder()
                                   .url(HOST + "/api/kickout/logout?token=" + TokenHolder.getToken())
                                   .header("Authorization", TokenRepository.getToken().getToken())
                                   .build());
  }

  public void setUsername(String userNameText) {
    username = userNameText;
  }

  public String getUsername() {
    return username;
  }
}
