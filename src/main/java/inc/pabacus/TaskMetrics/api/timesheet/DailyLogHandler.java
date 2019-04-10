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

@Service
public class DailyLogHandler implements DailyLogService {

  private DailyLogRepository repository;

  public DailyLogHandler() {
  }

  public DailyLogHandler(DailyLogRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<DailyLog> getAllLogs() {
    return populate();
  }

  @SuppressWarnings("all")
  private List<DailyLog> populate() {
    List<DailyLog> dailyLogs = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
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
