package inc.pabacus.TaskMetrics.api.timesheet.status;

import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.UserActivity;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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


  public List<UserActivity> unrecognizedLogs() {
    List<UserActivity> activities = activities();
//    System.out.println(activities);
    List<StatusUpdate> statusUpdates = statusUpdates();
    LocalDate today = LocalDate.now();
    List<UserActivity> activitiesToday = activities.stream()
        .filter(userActivity -> userActivity.getDate().equals(today.toString()))
        .collect(Collectors.toList());
//    System.out.println(activitiesToday);
    List<StatusUpdate> updatesToday = statusUpdates.stream()
        .filter(statusUpdate -> statusUpdate.getDate().equals(today.toString()))
        .collect(Collectors.toList());

    return activitiesToday.stream()
        .filter(activity -> updatesToday.stream()
            .anyMatch(statusUpdate -> {
//              System.out.println("Status");
//              System.out.println(statusUpdate);
//              System.out.println("----");
//              System.out.println("activity");
//              System.out.println(activity);
              LocalTime statusUpdateTime = LocalTime.parse(statusUpdate.getTime());
              LocalTime activityTime = LocalTime.parse(activity.getTime());

              Duration duration = Duration.between(statusUpdateTime, activityTime);

              long totalGap = Math.abs(duration.toMinutes());
              return (totalGap >= 5);
            }))
        .collect(Collectors.toList());
  }

  public List<UserActivity> unrecognizedTasks() {
    List<UserActivity> activities = activities();
    List<XpmTask> xpmTasks = xpmTasks();
    LocalDate today = LocalDate.now();
    List<UserActivity> activitiesToday = activities.stream()
        .filter(userActivity -> userActivity.getDate().equals(today.toString()))
        .collect(Collectors.toList());
    List<XpmTask> updatesToday = xpmTasks.stream()
        .filter(statusUpdate -> statusUpdate.getStartTime().equals(today.toString()))
        .collect(Collectors.toList());


    return activitiesToday.stream()
        .filter(activity -> updatesToday.stream()
            .anyMatch(statusUpdate -> {
//              System.out.println("Status");
//              System.out.println(statusUpdate);
//              System.out.println("----");
//              System.out.println("activity");
//              System.out.println(activity);
              LocalTime statusUpdateTime = LocalTime.parse(statusUpdate.getStartTime());
              LocalTime activityTime = LocalTime.parse(activity.getTime());

              Duration duration = Duration.between(statusUpdateTime, activityTime);

              long totalGap = Math.abs(duration.toMinutes());
              return (totalGap >= 5);
            }))
        .collect(Collectors.toList());
  }

}
