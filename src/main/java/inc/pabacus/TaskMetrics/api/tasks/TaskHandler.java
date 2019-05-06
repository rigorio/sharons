package inc.pabacus.TaskMetrics.api.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inc.pabacus.TaskMetrics.api.project.Project;
import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskHandler implements TaskService {

  private OkHttpClient client = new OkHttpClient();
  private ObjectMapper mapper = new ObjectMapper();
  private static final String HOST = "http://localhost:8080";
  private static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  private TaskWebRepository taskRepository;

  public TaskHandler() {
    taskRepository = new TaskWebRepository();
  }

  public TaskHandler(TaskWebRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public Task createTask(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public Project saveTask(Task task) {
    Project t = null;
    try {
      String jsonString = mapper.writeValueAsString(task);
      RequestBody body = RequestBody.create(JSON, jsonString);
      Call call = client.newCall(new Request.Builder()
                                     .url(HOST + "/api/project/task")
                                     .post(body)
                                     .build());
      ResponseBody responseBody = call.execute().body();
      t = mapper.readValue(responseBody.string(), new TypeReference<Project>() {});
      task.setId(t.getId());
    } catch (IOException e) {
      e.printStackTrace();
      return t;
    }
    return t;

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
