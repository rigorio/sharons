package inc.pabacus.TaskMetrics.api.timesheet;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DailyLogHandler implements DailyLogService {

  private DailyLogRepository repository;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
  private List<DailyLog> dailyLogs = new ArrayList<>();

  public DailyLogHandler() {
    populate();
  }

  public DailyLogHandler(DailyLogRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<DailyLog> getAllLogs() {
    return dailyLogs;
  }

  public String changeLog() {
    Optional<DailyLog> anyLog = getAllLogs().stream()
        .filter(dailyLog -> dailyLog.getDate().equals(LocalDate.now().toString()))
        .findAny();
    if (!anyLog.isPresent()) {
      dailyLogs.add(new DailyLog(LocalDate.now().toString(), formatter.format(LocalTime.now()), "", "", ""));
      return "IN";
    } else {
      DailyLog dailyLog = anyLog.get();
      if (dailyLog.getIn().length() > 1) {
        return "OTL";
      } else if (dailyLog.getOtl().length() < 1) {
        return "BFL";
      } else if (dailyLog.getBfl().length() < 1) {
        return "OUT";
      }
    }
    return "OUT";
  }

  @SuppressWarnings("all")
  private List<DailyLog> populate() {
    LocalTime now = LocalTime.now();
    formatter.format(now);
    dailyLogs.add(new DailyLog(LocalDate.now().minus(5, ChronoUnit.DAYS).toString(),
                               formatter.format(now.minus(5, ChronoUnit.HOURS)),
                               formatter.format(now.minus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(5, ChronoUnit.HOURS))));
    dailyLogs.add(new DailyLog(LocalDate.now().minus(4, ChronoUnit.DAYS).toString(),
                               formatter.format(now.minus(8, ChronoUnit.HOURS)),
                               formatter.format(now.minus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(5, ChronoUnit.HOURS))));
    dailyLogs.add(new DailyLog(LocalDate.now().minus(3, ChronoUnit.DAYS).toString(),
                               formatter.format(now.minus(5, ChronoUnit.HOURS)),
                               formatter.format(now.minus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(4, ChronoUnit.HOURS)),
                               formatter.format(now.plus(5, ChronoUnit.HOURS))));
    dailyLogs.add(new DailyLog(LocalDate.now().minus(2, ChronoUnit.DAYS).toString(),
                               formatter.format(now.minus(4, ChronoUnit.HOURS)),
                               formatter.format(now.minus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(5, ChronoUnit.HOURS))));
    dailyLogs.add(new DailyLog(LocalDate.now().minus(1, ChronoUnit.DAYS).toString(),
                               formatter.format(now.minus(5, ChronoUnit.HOURS)),
                               formatter.format(now.minus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(5, ChronoUnit.HOURS))));
    dailyLogs.add(new DailyLog(LocalDate.now().toString(),
                               formatter.format(now.minus(7, ChronoUnit.HOURS)),
                               formatter.format(now.minus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(3, ChronoUnit.HOURS)),
                               formatter.format(now.plus(7, ChronoUnit.HOURS))));


    return dailyLogs;
  }

}
