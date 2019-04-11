package inc.pabacus.TaskMetrics.utils;

import com.google.gson.Gson;
import inc.pabacus.TaskMetrics.api.hardware.HardwareData;
import inc.pabacus.TaskMetrics.api.hardware.HardwareService;
import inc.pabacus.TaskMetrics.api.hardware.WindowsHardwareHandler;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import inc.pabacus.TaskMetrics.api.software.SoftwareData;
import inc.pabacus.TaskMetrics.api.software.SoftwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareService;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpService {

  private OkHttpClient client = new OkHttpClient();


  public static void main(String[] args) throws IOException {
//    sendSoftware();
//    sendDisk();
//    sendJob();
    sendJobs();
  }

  private static void sendDisk() throws IOException {
    HardwareService service = new WindowsHardwareHandler();
    List<HardwareData> disks = service.getDisks();
    MediaType parse = MediaType.parse("application/json; charset=utf-8");
    String content = new Gson().toJson(disks);
    RequestBody body = RequestBody.create(parse, content);
    getCall(body, "http://localhost:8080/api/disks");
  }

  private static void sendSoftware() throws IOException {
    SoftwareService softwareService = new SoftwareHandler();
    List<SoftwareData> software = softwareService.getSoftware();

    MediaType parse = MediaType.parse("application/json; charset=utf-8");
    String content = new Gson().toJson(software);
    RequestBody body = RequestBody.create(parse, content);
    getCall(body, "http://localhost:8080/api/softwares");
  }

  private static void sendJob() throws IOException {
    Task task = new Task();
    task.setAuthor("Rigo Sarmiento");
    task.setDateCreated("01/12/19");
    task.setDescription("Simulate how the improbable becomes impossible");
    task.setTitle("Test flight");
    task.setProgress(Progress.SEVENTY_FIVE);
    task.setStatus(Status.IN_PROGRESS);


    MediaType parse = MediaType.parse("application/json; charset=utf-8");
    String content = new Gson().toJson(task);
    RequestBody body = RequestBody.create(parse, content);
    getCall(body, "http://localhost:8080/api/job");
  }

  private static void sendJobs() throws IOException {
    String response = new OkHttpClient().newCall(new Request.Builder()
                                                     .url("http://localhost:8080/api/jobs")
                                                     .build())
        .execute().body().string();
    Object k = ((List<Map>) new Gson().fromJson(response, List.class)).stream().findAny().get().get("id");
    String kwan = k.toString();
    Long id = (long) Double.parseDouble(kwan);
    Task task = new Task();
    task.setId(id);
    task.setAuthor("Rigo Sarmiento");
    task.setDateCreated("01/12/19");
    task.setDescription("Simulate how the improbable becomes impossible");
    task.setTitle("Test flight");
    task.setProgress(Progress.ONE_HUNDRED);
    task.setStatus(Status.DONE);


    MediaType parse = MediaType.parse("application/json; charset=utf-8");
    String content = new Gson().toJson(task);
    RequestBody body = RequestBody.create(parse, content);
    getCall(body, "http://localhost:8080/api/job");
  }

  private static void getCall(RequestBody body, String url) throws IOException {
    new OkHttpClient().newCall(new Request.Builder()
                                   .url(url)
                                   .post(body)
                                   .build()
                              ).execute();
  }

}
