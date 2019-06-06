package inc.pabacus.TaskMetrics.desktop.tasks.xpm;

import java.util.ArrayList;
import java.util.List;

public class XpmTaskHandler {

  private List<XpmTask> xpmTasks = generateMockups();

  public List<XpmTask> getTasks() {
    return xpmTasks;
  }

  public void save(XpmTask task) {
    xpmTasks.removeIf(xpm -> xpm.getId().equals(task.getId()));
    xpmTasks.add(task);
  }

  public List<XpmTask> generateMockups() {
    ArrayList<XpmTask> xpmTasks = new ArrayList<>();
    xpmTasks.add(XpmTask.builder()
                     .title("Maintenance")
                     .description("Provide maintenance support")
                     .status("In Progress")
                     .totalTime("2.4")
                     .job("IT Support")
                     .client("DBA")
                     .assignee("Rigo Sarmiento")
                     .build());
    xpmTasks.add(XpmTask.builder()
                     .title("Troubleshooting")
                     .description("Troubleshoot all problematic softwarre")
                     .status("In Progress")
                     .totalTime("1.5")
                     .job("IT Support")
                     .client("DBA")
                     .assignee("Rigo Sarmiento")
                     .build());
    xpmTasks.add(XpmTask.builder()
                     .title("Monitoring")
                     .description("Monitor whatever needs to be monitored")
                     .status("In Progress")
                     .totalTime("3.1")
                     .job("IT Support")
                     .client("DBA")
                     .assignee("Rigo Sarmiento")
                     .build());
    xpmTasks.add(XpmTask.builder()
                     .title("Backup Recovery")
                     .description("Recover backup data because of problems")
                     .status("In Progress")
                     .totalTime("2.6")
                     .job("IT Support")
                     .client("DBA")
                     .assignee("Rigo Sarmiento")
                     .build());
    xpmTasks.add(XpmTask.builder()
                     .title("Development and Training")
                     .description("Improvement of oneself through years of hardship")
                     .status("In Progress")
                     .totalTime("1.2")
                     .job("IT Support")
                     .client("DBA")
                     .assignee("Rigo Sarmiento")
                     .build());
    xpmTasks.add(XpmTask.builder()
                     .title("Clearing checks")
                     .description("I don't really know accounting tasks")
                     .status("In Progress")
                     .totalTime("4.2")
                     .job("Accounting")
                     .client("DBA")
                     .assignee("Rigo Sarmiento")
                     .build());
    xpmTasks.add(XpmTask.builder()
                     .title("Verifying statutory numbers")
                     .description("Maybe this is an accounting task")
                     .status("In Progress")
                     .totalTime("2.2")
                     .job("Accounting")
                     .client("DBA")
                     .assignee("Rigo Sarmiento")
                     .build());
    return xpmTasks;
  }

}
