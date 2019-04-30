package inc.pabacus.TaskMetrics.desktop.tracker;

import inc.pabacus.TaskMetrics.api.tasks.TaskFXAdapter;

public class TrackHandler {
  private static TaskFXAdapter selectedTask;

  public static TaskFXAdapter getSelectedTask() {
    return selectedTask;
  }

  public static void setSelectedTask(TaskFXAdapter selectedTask) {
    TrackHandler.selectedTask = selectedTask;
  }
}
