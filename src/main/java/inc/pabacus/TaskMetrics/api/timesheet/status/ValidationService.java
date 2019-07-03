package inc.pabacus.TaskMetrics.api.timesheet.status;

import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.UserActivity;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;

import java.util.List;

public class ValidationService {

  private ActivityHandler activityHandler;
  private StatusUpdateHandler statusUpdateHandler;
  private XpmTaskWebHandler xpmHandler;

  public ValidationService() {
    activityHandler = BeanManager.activityHandler();
    statusUpdateHandler = BeanManager.statusUpdateHandler();
    xpmHandler = BeanManager.xpmTaskHandler();
  }


  public List<UserActivity> activities() {
    return activityHandler.all();
  }

  public List<StatusUpdate> statusUpdates() {
    return statusUpdateHandler.all();
  }

  public List<XpmTask> xpmTasks() {
    return xpmHandler.findAll();
  }


  private void validation() {


  }

}
