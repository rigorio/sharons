package inc.pabacus.TaskMetrics.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.api.generateToken.UsernameHolder;
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
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private StringCacheService stringCacheService = new StringCacheService();

  public XpmTaskWebHandler() {
    HOST = new HostConfig().getHost();
  }

  @SuppressWarnings("all")
  public XpmTask save(XpmTask task) {
    try {
      XpmTaskPostEntity xpmDto = new XpmTaskPostEntity();
      String jsonString = mapper.writeValueAsString(xpmDto);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      XpmTask xpmTask;
      xpmTask = mapper.readValue(responseBody.string(), new TypeReference<XpmTask>() {});
      xpmTask.setId(xpmTask.getId());
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return task;
  }

  public void save(XpmTaskPostEntity dto_save) {
    try {
      String jsonString = mapper.writeValueAsString(dto_save);
      System.out.println(jsonString);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
//      XpmTask xpmTask;
//      xpmTask = mapper.readValue(responseBody.string(), new TypeReference<XpmTask>() {});
//      xpmTask.setId(xpmTask.getId());
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
  }

  public void otherSave(TaskCreationDTO dto) {
    try {
      String jsonString = mapper.writeValueAsString(dto);
      System.out.println("i wanted " + jsonString);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      String responseString = responseBody.string();
      System.out.println("response string is " + responseString);
    } catch (IOException e) {
      logger.warn(e.getMessage());
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
      logger.warn(e.getMessage());
    }
  }

  public List<XpmTask> findAll() {
    List<XpmTask> tasks = new ArrayList<>();
    try {

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet")
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .build());
      ResponseBody body = call.execute().body();
      String jsonString = body.string();
      tasks = mapper.readValue(jsonString, new TypeReference<List<XpmTask>>() {});
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return tasks;
  }

  public List<XpmTask> findByJobTask(Long jobTaskId) {
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
    } catch (IOException e) {
      logger.warn(e.getMessage());
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
      logger.warn(e.getMessage());
    }

    Optional<Assignee> any = assignees.stream().filter(assignee -> assignee.getUserName().equals(UsernameHolder.username))
        .findAny();
    Assignee assignee;
    assignee = any.orElseGet(() -> new Assignee(1L, UsernameHolder.username));
    return assignee;
  }

  public void edit(XpmTaskPostEntity helpMe) {
    try {
      String jsonString = mapper.writeValueAsString(helpMe);
      System.out.println("editing");
      System.out.println(jsonString);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/user/timesheet/" + helpMe.getId())
                                     .addHeader("Authorization", stringCacheService.get(CacheKey.TRIBELY_TOKEN))
                                     .put(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      XpmTask xpmTask;
      String respostring = responseBody.string();
      System.out.println("respo");
      System.out.println(respostring);
      xpmTask = mapper.readValue(respostring, new TypeReference<XpmTask>() {});
      xpmTask.setId(xpmTask.getId());
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
  }
}
