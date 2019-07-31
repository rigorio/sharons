package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.api.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import inc.pabacus.TaskMetrics.utils.SslUtil;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

public class JobTaskHandler {

  private static final Logger logger = Logger.getLogger(JobTaskHandler.class);
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private StringCacheService cacheService = new StringCacheService();
  private String tribelyToken = cacheService.get(CacheKey.TRIBELY_TOKEN);

  public JobTaskHandler() {
    HOST = new HostConfig().getHost();
  }

  public List<JobTask> allJobTasks() {
    List<JobTask> jobTasks = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/job/tasks")
                                     .addHeader("Authorization", tribelyToken)
                                     .build());
      String jsonString = call.execute().body().string();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, FALSE);
      jobTasks = mapper.readValue(jsonString, new TypeReference<List<JobTask>>() {});
    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e.getMessage());
    }
    return jobTasks;
  }

  public void createJobTask(Long jobId, Long taskId, String description) { // idk why not workign yet
    try {

      JobTaskCreationEntity jtce = new JobTaskCreationEntity(taskId, description);
      String s = "";
      RequestBody requestBody;
      if (taskId != null) {
        s = mapper.writeValueAsString(jtce);
        requestBody = RequestBody.create(JSON, s);
      } else {
        requestBody = RequestBody.create(JSON, "{}");
      }
      Request.Builder request = new Request.Builder()
          .url(HOST + "/api/job/tasks/create/" + jobId)
          .post(requestBody)
          .addHeader("Authorization", tribelyToken);

      Call call = client.newCall(request.build());
      String string = call.execute().body().string();

    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e.getMessage());
    }
  }

  public List<Job> allJobs() {
    List<Job> jobs = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/jobs")
                                     .addHeader("Authorization", tribelyToken)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      jobs = mapper.readValue(responseBody.string(), new TypeReference<List<Job>>() {});
    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e.getMessage());
    }
    return jobs;
  }

  public List<Task> allTasks() {
    List<Task> tasks = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/jobs/tasks")
                                     .addHeader("Authorization", tribelyToken)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      tasks = mapper.readValue(responseBody.string(), new TypeReference<List<Task>>() {});
    } catch (
        IOException e) {
      logger.warn(e.getMessage());
    }
    return tasks;
  }

  private class JobTaskCreationEntity {
    private Long taskId;
    private String description;

    public JobTaskCreationEntity(Long taskId, String description) {
      this.taskId = taskId;
      this.description = description;
    }

    public Long getTaskId() {
      return taskId;
    }

    public void setTaskId(Long taskId) {
      this.taskId = taskId;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }
}
