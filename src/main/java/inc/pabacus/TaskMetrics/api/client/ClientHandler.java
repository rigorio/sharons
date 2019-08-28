package inc.pabacus.TaskMetrics.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import inc.pabacus.TaskMetrics.utils.cacheService.LocalCacheHandler;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {

  private static final Logger logger = Logger.getLogger(JobTaskHandler.class);
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private StringCacheService cacheService = new StringCacheService();

  public List<Client> allClients() {
    List<Client> clients = new ArrayList<>();
    try {

      Call call = client.newCall(new Request.Builder()
                                     .url(new HostConfig().getHost() + "/api/jobs/clients")
                                     .addHeader("Authorization", LocalCacheHandler.getTribelyToken())
                                     .build());
      String responseString = call.execute().body().string();
      System.out.println("clients are " + responseString);
      clients = mapper.readValue(responseString, new TypeReference<List<Client>>() {});
      return clients;
    } catch (IOException e) {
      e.printStackTrace();
      return clients;
    }
  }

}
