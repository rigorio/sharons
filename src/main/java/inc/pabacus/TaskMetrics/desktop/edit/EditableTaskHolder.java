package inc.pabacus.TaskMetrics.desktop.edit;

import inc.pabacus.TaskMetrics.api.tasks.TaskFXAdapter;

public class EditableTaskHolder {

  private static TaskFXAdapter task = null;

  public static TaskFXAdapter getTask() {
    return task;
  }

  public static void setTask(TaskFXAdapter task) {
    EditableTaskHolder.task = task;
  }
}
