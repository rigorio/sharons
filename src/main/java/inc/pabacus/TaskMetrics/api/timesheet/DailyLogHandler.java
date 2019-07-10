package inc.pabacus.TaskMetrics.api.timesheet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.api.timesheet.logs.LogItem;
import inc.pabacus.TaskMetrics.desktop.breakTimer.BreakView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import inc.pabacus.TaskMetrics.utils.HostConfig;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class DailyLogHandler implements DailyLogService {

  private static final Logger logger = Logger.getLogger(DailyLogHandler.class);
  private DailyLogRepository repository;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
  private List<DailyLog> dailyLogs = new ArrayList<>();
  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static String HOST;
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
  public ScheduledFuture<?> scheduledFuture;
  private HostConfig hostConfig = new HostConfig();

  public DailyLogHandler() {
    HOST = hostConfig.getHost();
    repository = new DailyLogWebRepository();
  }

  @Override
  public List<DailyLog> getAllLogs() {
    return repository.findAll();
  }

  @Override
  public DailyLog changeLog(String status) {
    DailyLog dailyLog = getToday();
    try {
      LogItem logItem = LogItem.builder()
          .id(dailyLog.getId())
          .status(status)
          .time(formatter.format(LocalTime.now()))
          .build();
      String jsonString = mapper.writeValueAsString(logItem);
      RequestBody body = RequestBody.create(JSON, jsonString);

      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/log/update")
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())
                                     .post(body)
                                     .build());
      String responseString = call.execute().body().string();
      dailyLog = mapper.readValue(responseString, new TypeReference<DailyLog>() {});
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return dailyLog;
  }

  public DailyLog getToday() {
    DailyLog dailyLog = null;

    try {
      String path = "/api/logs?date=" + LocalDate.now();
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + path)
                                     .addHeader("Authorization", TokenRepository.getToken().getToken())

                                     .build());
      String responseString = call.execute().body().string();
      dailyLog = mapper.readValue(responseString, new TypeReference<DailyLog>() {});
    } catch (IOException e) {
      logger.warn(e.getMessage());
    }
    return dailyLog;
  }

  public void checkIfUserIsBreak() {
    ActivityHandler activityHandler = new ActivityHandler();

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable command = () -> Platform.runLater(() -> {

      if (activityHandler.getLastLog().equalsIgnoreCase("break")) {
        close();
        GuiManager.getInstance().displayView(new BreakView());
        notification("Break");
      } else if (activityHandler.getLastLog().equalsIgnoreCase("lunch") || activityHandler.getLastLog().equalsIgnoreCase("lunch break")) {
        close();
        GuiManager.getInstance().displayView(new BreakView());
        notification("Lunch Break");
      }
    });

    scheduledFuture = executor.scheduleAtFixedRate(command, 0, 1, TimeUnit.SECONDS);
  }

  public void close() {
    scheduledFuture.cancel(true);
  }

  private void notification(String notif) {
    Notifications notifications = Notifications.create()
        .title("TRIBELY")
        .text("Status change to " + notif)
        .position(Pos.BOTTOM_RIGHT)
        .hideAfter(Duration.seconds(5));
    notifications.darkStyle();
    notifications.showWarning();
  }
}
