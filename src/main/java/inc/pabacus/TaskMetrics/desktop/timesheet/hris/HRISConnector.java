package inc.pabacus.TaskMetrics.desktop.timesheet.hris;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.utils.SslUtil;
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
  private String bearer = "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjQiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoicmlnb3JpbyIsIkFzcE5ldC5JZGVudGl0eS5TZWN1cml0eVN0YW1wIjoiR1dSU1k1T0lINVhNSkZJVkxGNTNFRlZBNVFFS09BWTMiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJFbXBsb3llZSIsImh0dHA6Ly93d3cuYXNwbmV0Ym9pbGVycGxhdGUuY29tL2lkZW50aXR5L2NsYWltcy90ZW5hbnRJZCI6IjEiLCJzdWIiOiI0IiwianRpIjoiNjE4NGVhZjgtNGU3OS00MGViLWE2MTQtYzE2YThjZTQ2NjE2IiwiaWF0IjoxNTYzODQwMDA4LCJuYmYiOjE1NjM4NDAwMDgsImV4cCI6MTU2MzkyNjQwOCwiaXNzIjoiSFVSRVlfU1RBR0lORyIsImF1ZCI6IkhVUkVZX1NUQUdJTkcifQ.lUz1K-wf50WvXHp-Pgf7bZ_Pk2K_05uxmW-L6jVf-ZE";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public List<HRISTimeLog> hrisLogs() {
    try {
      String employeeId = "f6befdfe-8876-4b6a-f503-08d704e3effb";
      String logDate = "2019-07-23";
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/services/app/EmployeeTimeLog/GetAllNotDeletedByEmployeeIdAndDate?employeeId=" + employeeId + "&logDate=" + logDate)
                                     .addHeader("Authorization", bearer)
                                     .build());
      String string = call.execute().body().string();
      Map<String, Object> o = mapper.readValue(string, new TypeReference<Map<String, Object>>() {});
      Object items = ((Map<String, Object>) o.get("result")).get("items");
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, FALSE);
      List<HRISTimeLog> logs = mapper.readValue(mapper.writeValueAsString(items), new TypeReference<List<HRISTimeLog>>() {});
      logs = logs.stream()
          .peek(hrisTimeLog -> {
            String time = hrisTimeLog.getTime();
            hrisTimeLog.setDate(logDate);
            hrisTimeLog.setTime(time.split("T")[1]);
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
    String timeLog = LocalDate.now().toString() + "T" + formatter.format(LocalTime.now());
    InetAddress localHost = null;
    try {
      localHost = InetAddress.getLocalHost();
      CrazyHrisEntity crazyHrisEntity = CrazyHrisEntity.builder()
          .employeeId("f6befdfe-8876-4b6a-f503-08d704e3effb")
          .timeLog(timeLog)
          .timeLogTypeId(timeLogTypeId)
          .ipAddress(localHost.getHostAddress())
          .shiftDate("2019-07-23T00:00:00+00:00")
          .id(0)
          .build();
      String requestString = mapper.writeValueAsString(crazyHrisEntity);
      RequestBody requestBody = RequestBody.create(JSON, requestString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/services/app/EmployeeTimeLog/Create")
                                     .addHeader("Authorization", bearer)
                                     .post(requestBody)
                                     .build());
      String string = call.execute().body().string();
      System.out.println("nani ?");
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
