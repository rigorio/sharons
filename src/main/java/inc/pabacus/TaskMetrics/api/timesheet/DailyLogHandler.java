package inc.pabacus.TaskMetrics.api.timesheet;

import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyLogHandler implements DailyLogService {

  private DailyLogRepository repository;

  public DailyLogHandler(DailyLogRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<DailyLog> getAllLogs() {
    return repository.findAll();
  }


}
