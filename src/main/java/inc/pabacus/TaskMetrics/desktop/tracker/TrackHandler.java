package inc.pabacus.TaskMetrics.desktop.tracker;

import inc.pabacus.TaskMetrics.api.tasks.TaskAdapter;

public class TrackHandler {
  private static TaskAdapter selectedTask;

  public static TaskAdapter getSelectedTask() {
    return selectedTask;
  }

  public static void setSelectedTask(TaskAdapter selectedTask) {
    TrackHandler.selectedTask = selectedTask;
  }
}
