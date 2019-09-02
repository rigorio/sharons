package inc.pabacus.TaskMetrics.api.timesheet.status;

import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.ActivityTimestamp;
import inc.pabacus.TaskMetrics.api.tasks.Task;
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


  public List<ActivityTimestamp> activities() {
    return activityHandler.allTimestamps();
  }

  public List<StatusUpdate> statusUpdates() {
    return statusUpdateHandler.all();
  }

  public List<Task> xpmTasks() {
    return xpmHandler.findAll();
  }


  public List<ActivityTimestamp> unrecognizedLogs() {
    List<ActivityTimestamp> activities = activities();
//    System.out.println(activities);
    List<StatusUpdate> statusUpdates = statusUpdates();
    LocalDate today = LocalDate.now();
    List<ActivityTimestamp> activitiesToday = activities.stream()
        .filter(activityTimestamp -> activityTimestamp.getDate().equals(today.toString()))
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

  public List<ActivityTimestamp> unrecognizedTasks() {
    List<ActivityTimestamp> activities = activities();
    List<Task> tasks = xpmTasks();
    LocalDate today = LocalDate.now();
    List<ActivityTimestamp> activitiesToday = activities.stream()
        .filter(activityTimestamp -> activityTimestamp.getDate().equals(today.toString()))
        .collect(Collectors.toList());
    List<Task> updatesToday = tasks.stream()
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
