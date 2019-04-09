package inc.pabacus.TaskMetrics.api.tasks;

import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskHandler implements TaskService {

  private TaskRepository taskRepository;

  public TaskHandler(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public Task saveTask(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public Optional<Task> getTask(Long id) {
    return taskRepository.findById(id);
  }

  @Override
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  @Override
  public List<TaskFXAdapter> convertTasks(List<Task> tasks) {
    return tasks.stream().parallel()
        .map(TaskFXAdapter::new)
        .collect(Collectors.toList());
  }

  @Override
  public List<Task> searchTasks(String keyword) {
    return taskRepository.findAll().stream()
        .filter(task -> task.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
            task.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
            task.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Task> searchTasks(Status status) {
    return taskRepository.findAll().stream()
        .filter(task -> task.getStatus().equals(Status.BACKLOG))
        .collect(Collectors.toList());
  }

  @Override
  public List<Task> searchTasks(Progress progress) {
    return taskRepository.findAll().stream()
        .filter(task -> task.getProgress().equals(Progress.FIFTY))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteTask(Task task) {
    taskRepository.delete(task);
  }

  @Override
  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
  }
}
