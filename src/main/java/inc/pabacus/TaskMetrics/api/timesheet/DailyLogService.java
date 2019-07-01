package inc.pabacus.TaskMetrics.api.timesheet;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;

import java.util.List;

/**
 * @see DailyLogHandler
 */
public interface DailyLogService {


  List<DailyLog> getAllLogs();

  DailyLog changeLog(String status);
}
