package inc.pabacus.TaskMetrics.desktop.tracker;

import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTaskAdapter;

public class TrackHandler {
  private static XpmTaskAdapter selectedTask;

  public static XpmTaskAdapter getSelectedTask() {
    return selectedTask;
  }

  public static void setSelectedTask(XpmTaskAdapter selectedTask) {
    TrackHandler.selectedTask = selectedTask;
  }
}
