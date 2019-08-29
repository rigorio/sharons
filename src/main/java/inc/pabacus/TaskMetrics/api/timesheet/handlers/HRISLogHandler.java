package inc.pabacus.TaskMetrics.api.timesheet.handlers;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.desktop.timesheet.hris.HRISConnector;
import inc.pabacus.TaskMetrics.desktop.timesheet.hris.HRISTimeLog;

import java.util.List;

public class HRISLogHandler implements LogService {

  private HRISConnector hrisConnector;

  public HRISLogHandler() {
    hrisConnector = new HRISConnector();
  }

  @Override
  public void changeLog(String log) {
    hrisConnector.saveLog(log);
  }

  @Override
  public List<DailyLog> allConvertedDailyLogs() {
    return hrisConnector.allConvertedDailyLogs();
  }

  @Override
  public List<DailyLog> getLogOfDate(String date) {
    return hrisConnector.getLogOfDate(date);
  }

  @Override
  public String getLatest() {
    List<HRISTimeLog> logs = hrisConnector.hrisLogs();
    if (logs.isEmpty())
      return "Time Out";
    HRISTimeLog hrisTimeLog = logs.get(logs.size() - 1);
    System.out.println(hrisTimeLog);
    return hrisTimeLog.getStatus();
  }
}
