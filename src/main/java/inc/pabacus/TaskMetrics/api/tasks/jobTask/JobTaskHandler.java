package inc.pabacus.TaskMetrics.api.tasks.jobTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.utils.logs.LogHelper;
import inc.pabacus.TaskMetrics.utils.web.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

public class JobTaskHandler {

  private static final Logger logger = Logger.getLogger(JobTaskHandler.class);
  private LogHelper logHelper;
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private StringCacheService stringCacheService = new StringCacheService();

  public JobTaskHandler() {
    HOST = new HostConfig().getHost();
    logHelper = new LogHelper(logger);
    logHelper.setClass(JobTaskHandler.class);
  }

  public List<JobTask> allJobTasks() {
    List<JobTask> jobTasks = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/job/tasks")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
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
      System.out.println(jobId + " -- " + s);
      Request.Builder request = new Request.Builder()
          .url(HOST + "/api/job/tasks/create/" + jobId)
          .post(requestBody)
          .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN));

      Call call = client.newCall(request.build());
      String string = call.execute().body().string();
      System.out.println("Job answer ");
      System.out.println(string);

    } catch (IOException e) {
      e.printStackTrace();
      logger.warn(e.getMessage());
    }
  }

  public List<Job> allJobs(boolean byDepartment) {
    logHelper.logInfo("Retrieving all jobs", null);
    List<Job> jobs = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/jobs?byDepartment=" + byDepartment)
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String jsonString = responseBody.string();
      logHelper.logInfo("Retrieved all jobs", jsonString);
      jobs = mapper.readValue(jsonString, new TypeReference<List<Job>>() {});
    } catch (IOException e) {
      logHelper.logError("Exception encountered", e);
    }
    return jobs;
  }

  public List<Task> allTasks() {
    List<Task> tasks = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/jobs/tasks")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
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
