package inc.pabacus.TaskMetrics.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.tasks.dto.TaskCreationDTO;
import inc.pabacus.TaskMetrics.api.tasks.dto.TaskEditDTO;
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
public class TaskConnector {

  private static final Logger logger = Logger.getLogger(TaskConnector.class);
  private LogHelper logHelper;
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private StringCacheService stringCacheService = new StringCacheService();

  public TaskConnector() {
    HOST = new HostConfig().getHost();
    logHelper = new LogHelper(logger);
  }
//
//  @SuppressWarnings("all")
//  public Task save(Task task) {
//    try {
//      XpmTaskPostEntity xpmDto = new XpmTaskPostEntity();
//      logHelper.logInfo("Saving task", xpmDto);
//      String jsonString = mapper.writeValueAsString(xpmDto);
//      RequestBody body = RequestBody.create(JSON, jsonString);
//      Call call = client.newCall(new Request.Builder()
//                                     .url(HOST + "/api/user/timesheet")
//                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
//                                     .post(body)
//                                     .build());
//      ResponseBody responseBody = call.execute().body();
//      String responseString = responseBody.string();
//      logHelper.logInfo("Response of save", responseString);
//      Task xpmTask;
//      xpmTask = mapper.readValue(responseString, new TypeReference<Task>() {});
//      xpmTask.setId(xpmTask.getId());
//    } catch (IOException e) {
//      logHelper.logError("Exception caught", e.getMessage());
//    }
//    return task;
//  }
//
//  public void save(XpmTaskPostEntity dto_save) {
//    try {
//      logHelper.logInfo("Saving xpmtaskpost entity", dto_save);
//      String jsonString = mapper.writeValueAsString(dto_save);
//      RequestBody body = RequestBody.create(JSON, jsonString);
//      Call call = client.newCall(new Request.Builder()
//                                     .url(HOST + "/api/user/timesheet")
//                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
//                                     .post(body)
//                                     .build());
//      ResponseBody responseBody = call.execute().body();
//      String responseString = responseBody.string();
//      logHelper.logInfo("Response of saving xpmtaskpost entity", responseString);
////      Task xpmTask;
////      xpmTask = mapper.readValue(responseBody.string(), new TypeReference<Task>() {});
////      xpmTask.setId(xpmTask.getId());
//    } catch (IOException e) {
//      logHelper.logError("Exception caught", e.getMessage());
//    }
//  }

  public void save(TaskCreationDTO dto) {
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

  public Optional<Task> findById(Long id) {
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

  public List<Task> findAll() {
    logHelper.logInfo("Retrieving all tasks", null);
    List<Task> tasks = new ArrayList<>();
    try {

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      logHelper.logInfo("Retrieved all tasks", jsonString);
      tasks = mapper.readValue(jsonString, new TypeReference<List<Task>>() {});
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
    return tasks;
  }

  public List<Task> findByJobTask(Long jobTaskId) {
    logHelper.logInfo("Find task by jobId", null);
    List<Task> tasks = new ArrayList<>();
    try {
//      System.out.println("token " + stringCacheService.get(CacheKey.TRIBELY_TOKEN));
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet/jobtask/" + jobTaskId)
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      tasks = mapper.readValue(jsonString, new TypeReference<List<Task>>() {});
      logHelper.logInfo("Response of tasks by jobId", tasks);
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
    return tasks;
  }

/*
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
*/

  public void edit(TaskEditDTO taskEditDTO) {
    try {
      logHelper.logInfo("Editing a task", taskEditDTO);
      String jsonString = mapper.writeValueAsString(taskEditDTO);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet/" + taskEditDTO.getId())
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .put(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      Task task;
      String respostring = responseBody.string();
      logHelper.logInfo("Response of editing a task", respostring);
      task = mapper.readValue(respostring, new TypeReference<Task>() {});
      task.setId(task.getId());
    } catch (IOException e) {
      logHelper.logError("Exception caught", e.getMessage());
    }
  }
}
