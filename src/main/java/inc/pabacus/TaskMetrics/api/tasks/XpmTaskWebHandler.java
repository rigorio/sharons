package inc.pabacus.TaskMetrics.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.UsernameHolder;
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
import java.util.Optional;

@SuppressWarnings("all")
public class XpmTaskWebHandler {

  private static final Logger logger = Logger.getLogger(XpmTaskWebHandler.class);
  private LogHelper logHelper;
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private StringCacheService stringCacheService = new StringCacheService();

  public XpmTaskWebHandler() {
    HOST = new HostConfig().getHost();
    logHelper = new LogHelper(logger);
    logHelper.setClass(XpmTaskWebHandler.class);
  }

  @SuppressWarnings("all")
  public XpmTask save(XpmTask task) {
    try {
      XpmTaskPostEntity xpmDto = new XpmTaskPostEntity();
      logHelper.logInfo("Saving task", xpmDto);
      String jsonString = mapper.writeValueAsString(xpmDto);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      logHelper.logInfo("Response of save", responseString);
      XpmTask xpmTask;
      xpmTask = mapper.readValue(responseString, new TypeReference<XpmTask>() {});
      xpmTask.setId(xpmTask.getId());
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
    return task;
  }

  public void save(XpmTaskPostEntity dto_save) {
    try {
      logHelper.logInfo("Saving xpmtaskpost entity", dto_save);
      String jsonString = mapper.writeValueAsString(dto_save);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      logHelper.logInfo("Response of saving xpmtaskpost entity", responseString);
//      XpmTask xpmTask;
//      xpmTask = mapper.readValue(responseBody.string(), new TypeReference<XpmTask>() {});
//      xpmTask.setId(xpmTask.getId());
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
  }

  public void otherSave(TaskCreationDTO dto) {
    try {
      logHelper.logInfo("Other save for task", dto);
      String jsonString = mapper.writeValueAsString(dto);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      logHelper.logInfo("Response of other save", responseString);
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
  }

  public Optional<XpmTask> findById(Long id) {
    return findAll().stream()
        .filter(task -> task.getId().equals(id))
        .findAny();
  }

  public void deleteById(Long id) {
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet/" + id)
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .delete()
                                     .build());
      call.execute();
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
  }

  public List<XpmTask> findAll() {
    logHelper.logInfo("Retrieving all tasks", null);
    List<XpmTask> tasks = new ArrayList<>();
    try {

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      logHelper.logInfo("Retrieved all tasks", jsonString);
      tasks = mapper.readValue(jsonString, new TypeReference<List<XpmTask>>() {});
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
    return tasks;
  }

  public List<XpmTask> findByJobTask(Long jobTaskId) {
    logHelper.logInfo("Find task by jobId", null);
    List<XpmTask> tasks = new ArrayList<>();
    try {
//      System.out.println("token " + stringCacheService.get(CacheKey.TRIBELY_TOKEN));
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet/jobtask/" + jobTaskId)
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      tasks = mapper.readValue(jsonString, new TypeReference<List<XpmTask>>() {});
      logHelper.logInfo("Response of tasks by jobId", tasks);
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
    return tasks;
  }

  public Assignee getAssignee() {
    List<Assignee> assignees = new ArrayList<>();
    try {

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/jobs/assignees")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      assignees = mapper.readValue(jsonString, new TypeReference<List<Assignee>>() {});

    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }

    Optional<Assignee> any = assignees.stream().filter(assignee -> assignee.getUserName().equals(UsernameHolder.username))
        .findAny();
    Assignee assignee;
    assignee = any.orElseGet(() -> new Assignee(1L, UsernameHolder.username));
    return assignee;
  }

  public void edit(XpmTaskPostEntity helpMe) {
    try {
      logHelper.logInfo("Editing a task", helpMe);
      String jsonString = mapper.writeValueAsString(helpMe);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet/" + helpMe.getId())
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .put(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      XpmTask xpmTask;
      String respostring = responseBody.string();
      logHelper.logInfo("Response of editing a task", respostring);
      xpmTask = mapper.readValue(respostring, new TypeReference<XpmTask>() {});
      xpmTask.setId(xpmTask.getId());
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
  }
}
