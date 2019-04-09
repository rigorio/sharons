package inc.pabacus.TaskMetrics.api.tasks;

import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;

import java.util.List;
import java.util.Optional;

/**
 * @see TaskHandler
 */
public interface TaskService {
  Task saveTask(Task task);

  Optional<Task> getTask(Long id);

  List<Task> getAllTasks();

  List<Task> searchTasks(String keyword);

  List<Task> searchTasks(Status status);

  List<Task> searchTasks(Progress progress);

  void deleteTask(Task task);

  void deleteTask(Long id);
}
