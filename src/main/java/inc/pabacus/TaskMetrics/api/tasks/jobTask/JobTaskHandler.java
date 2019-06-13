package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobTaskHandler {

  private static final Logger logger = Logger.getLogger(JobTaskHandler.class);
  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  @SuppressWarnings("all")
  public JobTask save(JobTask task) {
    try {
      String jsonString = mapper.writeValueAsString(task);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/task")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      JobTask xpmTask;
      xpmTask = mapper.readValue(responseBody.string(), new TypeReference<JobTask>() {});
      xpmTask.setId(xpmTask.getId());
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return task;
  }

  public Optional<JobTask> findById(Long id) {
    return findAll().stream()
        .filter(task -> task.getId().equals(id))
        .findAny();
  }

  public void deleteById(Long id) {
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/task/" + id)
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .delete()
                                     .build());
      call.execute();
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
  }

  public List<JobTask> findAll() {
    List<JobTask> tasks = new ArrayList<>();
    try {

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/tasks")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      tasks = mapper.readValue(jsonString, new TypeReference<List<JobTask>>() {});

    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return tasks;
  }

  public List<JobTask> findAllDefaults() {
    List<JobTask> tasks = new ArrayList<>();
    try {

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/tasks/defaults")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      tasks = mapper.readValue(jsonString, new TypeReference<List<JobTask>>() {});

    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return tasks;
  }

}
