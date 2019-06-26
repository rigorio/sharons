package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JobTaskHandler {

  private static final Logger logger = Logger.getLogger(JobTaskHandler.class);
  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public List<Job> allJobs() {
    List<Job> jobs = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/jobs")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());
      ResponseBody responseBody = call.execute().body();
      jobs = mapper.readValue(responseBody.string(), new TypeReference<List<Job>>() {});
    } catch (
        IOException e) {
      logger.warn(e.getMessage());
    }
    return jobs;
  }

  public List<Task> allTasks() {
    List<Task> tasks = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/jobs/tasks")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());
      ResponseBody responseBody = call.execute().body();
      tasks = mapper.readValue(responseBody.string(), new TypeReference<List<Task>>() {});
    } catch (
        IOException e) {
      logger.warn(e.getMessage());
    }
    return tasks;
  }

}
