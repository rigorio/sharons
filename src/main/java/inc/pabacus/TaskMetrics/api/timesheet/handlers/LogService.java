package inc.pabacus.TaskMetrics.api.timesheet.handlers;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;

import java.util.List;

public interface LogService {
  void changeLog(String log);

  List<DailyLog> allConvertedDailyLogs();

  List<DailyLog> getLogOfDate(String date);

  String getLatest();
}
