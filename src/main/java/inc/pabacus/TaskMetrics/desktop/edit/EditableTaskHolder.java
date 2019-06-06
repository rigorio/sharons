package inc.pabacus.TaskMetrics.desktop.edit;

import inc.pabacus.TaskMetrics.api.tasks.TaskFXAdapter;
import inc.pabacus.TaskMetrics.desktop.tasks.xpm.XpmTaskAdapter;

public class EditableTaskHolder {

  private static XpmTaskAdapter task = null;

  public static XpmTaskAdapter getTask() {
    return task;
  }

  public static void setTask(XpmTaskAdapter task) {
    EditableTaskHolder.task = task;
  }
}
