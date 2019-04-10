package inc.pabacus.TaskMetrics.api.timesheet;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;

import java.util.List;

public interface DailyLogService {


  List<DailyLog> getAllLogs();
}
