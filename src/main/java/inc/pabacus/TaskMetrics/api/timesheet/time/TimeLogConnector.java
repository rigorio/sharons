package inc.pabacus.TaskMetrics.api.timesheet.time;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogWebRepository;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.utils.web.HostConfig;
import inc.pabacus.TaskMetrics.utils.web.SslUtil;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class TimeLogConnector {


  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
  private static final Logger logger = Logger.getLogger(DailyLogWebRepository.class);
  private OkHttpClient client = SslUtil.getSslOkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public TimeLogConnector() {
    HOST = new HostConfig().getHost();
  }

  public List<DailyLog> allConvertedDailyLogs() {
    List<TimeLog> timeLogs = all();
    List<DailyLog> dailyLogs = new ArrayList<>();
    for (TimeLog timeLog : timeLogs) {
      Optional<DailyLog> anyLog = findDailyLog(dailyLogs, timeLog.getDate());
      if (anyLog.isPresent()) {
        DailyLog dailyLog = anyLog.get();
        int i = dailyLogs.indexOf(dailyLog);
        switcharoonie(dailyLog, timeLog);
        dailyLogs.set(i, dailyLog);
      } else {
        DailyLog dailyLog = new DailyLog();
        dailyLog.setDate(timeLog.getDate());
        dailyLog.setId(timeLog.getId());
        dailyLog.setUserId(timeLog.getUserId());
        dailyLogs.add(switcharoonie(dailyLog, timeLog));
      }
    }
    return dailyLogs;
  }

  public void saveTimeLog(String status) {
    try {
      TimeLog timeLog = TimeLog.builder()
          .id(0L)
          .userId(1L)
          .date(LocalDate.now().toString())
          .time(formatter.format(LocalTime.now()))
          .status(status)
          .build();
      String string = mapper.writeValueAsString(timeLog);
      RequestBody requestBody = RequestBody.create(JSON, string);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/timelogs")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(requestBody)
                                     .build());
      String jsonString = call.execute().body().string();
      System.out.println("response");
      System.out.println(jsonString);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<TimeLog> all() {

    List<TimeLog> timeLogs = new ArrayList<>();
    try {
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/timelogs")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .build());

      String jsonString = call.execute().body().string();
      timeLogs = mapper.readValue(jsonString, new TypeReference<List<TimeLog>>() {});
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return timeLogs;

  }

  public String lastLog() {
    TimeLog lastLog = all().get(all().size() - 1);
    return lastLog.getStatus();
  }

  private Optional<DailyLog> findDailyLog(List<DailyLog> dailyLogs, String date) {
    return dailyLogs.stream()
        .filter(dailyLog -> (dailyLog.getDate() != null ? dailyLog.getDate() : "").equals(date)).findAny();
  }

  private DailyLog switcharoonie(DailyLog dailyLog, TimeLog timeLog) {
    String status = timeLog.getStatus();
    String time = timeLog.getTime();
    switch (status) {
      case "Logged In":
        dailyLog.setIn(time);
        break;
      case "Lunch Break":
        dailyLog.setOtl(time);
        break;
      case "Back From Lunch":
        dailyLog.setBfl(time);
        break;
      case "Logged Out":
        dailyLog.setOut(time);
        break;
    }
    return dailyLog;
  }


}
