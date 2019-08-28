package inc.pabacus.TaskMetrics.desktop.timesheet.hris;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheKey;
import inc.pabacus.TaskMetrics.utils.cacheService.CacheService;
import inc.pabacus.TaskMetrics.utils.cacheService.LocalCacheHandler;
import inc.pabacus.TaskMetrics.utils.cacheService.StringCacheService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;

public class HRISConnector {

  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private String HOST = "https://hureyweb-staging.azurewebsites.net";
  //  private String bearer;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  private CacheService<CacheKey, String> cacheService;
  private final String employeeId;
  private LocalDate logDate;

  public HRISConnector() {
    cacheService = new StringCacheService();
    employeeId = cacheService.get(CacheKey.EMPLOYEE_ID);
//    bearer = cacheService.get(CacheKey.SHRIS_TOKEN);
    logDate = LocalDate.now();
  }

  public List<HRISTimeLog> hrisLogs() {
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(new StringCacheService().get(CacheKey.HUREY_HOST) + "/api/services/app/EmployeeTimeLog/GetAllNotDeletedByEmployeeIdAndDate?employeeId=" + employeeId + "&logDate=" + logDate.toString())
                                     .addHeader("Authorization", LocalCacheHandler.getHureyToken())
                                     .build());
      String string = call.execute().body().string();
      Map<String, Object> o = mapper.readValue(string, new TypeReference<Map<String, Object>>() {});
      Object items = ((Map<String, Object>) o.get("result")).get("items");
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, FALSE);
      List<HRISTimeLog> logs = mapper.readValue(mapper.writeValueAsString(items), new TypeReference<List<HRISTimeLog>>() {});
      logs = logs.stream()
          .peek(hrisTimeLog -> {
            String time = hrisTimeLog.getTime();
            hrisTimeLog.setDate(logDate.toString());
            hrisTimeLog.setTime(time.split("T")[1].replace("Z", ""));
          })
          .collect(Collectors.toList());
      return logs;
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  private int timeLogTypeId;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);

  public void saveLog(String status) {
    switch (status) {
      case "Time In":
        timeLogTypeId = 1;
        break;
      case "Lunch Break":
        timeLogTypeId = 3;
        break;
      case "Back From Break":
        timeLogTypeId = 4;
        break;
      case "Time Out":
        timeLogTypeId = 2;
        break;
    }
    String timeLog = logDate + "T" + formatter.format(LocalTime.now());
    InetAddress localHost = null;
    try {
      localHost = InetAddress.getLocalHost();
      CrazyHrisEntity crazyHrisEntity = CrazyHrisEntity.builder()
          .employeeId(employeeId)
          .timeLog(timeLog)
          .timeLogTypeId(timeLogTypeId)
          .ipAddress(localHost.getHostAddress())
          .shiftDate(logDate.toString() + "T00:00:00+00:00")
          .id(0)
          .build();
      String requestString = mapper.writeValueAsString(crazyHrisEntity);
      RequestBody requestBody = RequestBody.create(JSON, requestString);

      Call call = client.newCall(new Request.Builder()
                                     .url(new StringCacheService().get(CacheKey.HUREY_HOST) + "/api/services/app/EmployeeTimeLog/Create")
                                     .addHeader("Authorization", LocalCacheHandler.getHureyToken())
                                     .post(requestBody)
                                     .build());
      String string = call.execute().body().string();
      System.out.println("change logs response");
      System.out.println(string);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<DailyLog> allConvertedDailyLogs() {
    List<HRISTimeLog> timeLogs = hrisLogs();
    List<DailyLog> dailyLogs = new ArrayList<>();
    for (HRISTimeLog timeLog : timeLogs) {
      Optional<DailyLog> anyLog = findDailyLog(dailyLogs, timeLog.getDate());
      if (anyLog.isPresent()) {
        DailyLog dailyLog = anyLog.get();
        int i = dailyLogs.indexOf(dailyLog);
        switcharoonie(dailyLog, timeLog);
        dailyLogs.set(i, dailyLog);
      } else {
        DailyLog dailyLog = new DailyLog();
        dailyLog.setDate(timeLog.getDate());
        dailyLogs.add(switcharoonie(dailyLog, timeLog));
      }
    }
    return dailyLogs;
  }


  private Optional<DailyLog> findDailyLog(List<DailyLog> dailyLogs, String date) {
    return dailyLogs.stream()
        .filter(dailyLog -> (dailyLog.getDate() != null ? dailyLog.getDate() : "").equals(date)).findAny();
  }

  private DailyLog switcharoonie(DailyLog dailyLog, HRISTimeLog timeLog) {
    String status = timeLog.getStatus();
    String time = timeLog.getTime();
    switch (status) {
      case "Time In":
        dailyLog.setIn(time);
        break;
      case "Lunch Break":
        dailyLog.setOtl(time);
        break;
      case "Back From Break":
        dailyLog.setBfl(time);
        break;
      case "Time Out":
        dailyLog.setOut(time);
        break;
    }
    return dailyLog;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  private static class CrazyHrisEntity {
    private String employeeId;
    private String timeLog;
    private Integer timeLogTypeId;
    private String ipAddress;
    private String longitude;
    private String latitude;
    private String employeeCompanyId;
    private String employeeName;
    private String timeLogTypeName;
    private String shiftDate;
    private Integer id;
  }
}
