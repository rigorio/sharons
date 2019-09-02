package inc.pabacus.TaskMetrics.desktop.edit;

import inc.pabacus.TaskMetrics.api.tasks.TaskAdapter;

public class EditableTaskHolder {

  private static TaskAdapter task = null;

  public static TaskAdapter getTask() {
    return task;
  }

  public static void setTask(TaskAdapter task) {
    EditableTaskHolder.task = task;
  }
}
