package inc.pabacus.TaskMetrics.api.testingFacility;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.api.timesheet.time.TimeLog;
import inc.pabacus.TaskMetrics.api.timesheet.time.TimeLogConnector;
import org.junit.Ignore;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Tester {


  @Test
  @Ignore
  public void valueTester() {
    TimeLogConnector timeLogConnector = new TimeLogConnector();
    List<TimeLog> timeLogs = timeLogConnector.all();
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
    dailyLogs.forEach(System.out::println);
  }

  public Optional<DailyLog> findDailyLog(List<DailyLog> dailyLogs, String date) {
    return dailyLogs.stream()
        .filter(dailyLog -> (dailyLog.getDate() != null ? dailyLog.getDate() : "").equals(date)).findAny();
  }

  public DailyLog switcharoonie(DailyLog dailyLog, TimeLog timeLog) {
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

  @Test
  @Ignore
  public void testDoubleRoundup() {
    timer.accept(() -> {
      double a = 300.0 / 3600.0;
      String s = String.format("%.2f", a);
    });

    timer.accept(() -> {
      double jikan = 300.0 / 3600.0;
      DecimalFormat df = new DecimalFormat("0.00");
      String ans = "" + Double.parseDouble(df.format(jikan));
    });
  }

  private Consumer<Runnable> timer = (runnable) -> {
    Long start = System.nanoTime();
    runnable.run();
    System.out.println("Total time is " + (System.nanoTime() - start));
  };

//  @Test
//  public void testValidation() {
//    ValidationService validationService = new ValidationService();
//    List<ActivityTimestamp> invalidStatuses = validationService.validation();
//    System.out.println("-------------oooh");
//    invalidStatuses.forEach(System.out::println);
//  }
}
