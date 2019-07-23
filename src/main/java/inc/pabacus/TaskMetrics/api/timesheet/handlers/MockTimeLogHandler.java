package inc.pabacus.TaskMetrics.api.timesheet.handlers;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import inc.pabacus.TaskMetrics.api.timesheet.time.TimeLogConnector;
import inc.pabacus.TaskMetrics.utils.BeanManager;

import java.util.List;

public class MockTimeLogHandler implements LogService {

  private TimeLogConnector timeLogConnector;

  public MockTimeLogHandler() {
    timeLogConnector = BeanManager.timeLogConnector();
  }

  @Override
  public void changeLog(String log) {
    timeLogConnector.saveTimeLog(log);
  }

  @Override
  public List<DailyLog> allConvertedDailyLogs() {
    return timeLogConnector.allConvertedDailyLogs();
  }

  @Override
  public String getLatest() {
    return timeLogConnector.lastLog();
  }
}
