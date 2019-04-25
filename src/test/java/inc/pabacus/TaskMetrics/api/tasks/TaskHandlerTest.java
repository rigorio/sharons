package inc.pabacus.TaskMetrics.api.tasks;

import inc.pabacus.TaskMetrics.api.MockTaskDataProvider;
import inc.pabacus.TaskMetrics.api.tasks.options.Progress;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class TaskHandlerTest extends MockTaskDataProvider {

  private TaskService taskService;
  private TaskRepository taskRepository;
  private List<Task> list = getJobs();

  @Test
  public void testFindAll() {
    List<Task> allTasks = taskService.getAllTasks();
    assertEquals(list, allTasks);
  }

  @Test
  public void testSearchWithKeyword() {
    List<Task> tasks = taskService.searchTasks("keyword");
    list.removeIf(job -> job.getId().equals(0L));
    assertEquals(list, tasks);
  }

  @Test
  public void testFilterByStatus() {
    List<Task> tasks = taskService.searchTasks(Status.BACKLOG);
    List<Task> backLogTasks = getJobs().stream()
        .filter(job -> job.getStatus().equals(Status.BACKLOG))
        .collect(Collectors.toList());
    assertEquals(backLogTasks, tasks);
  }

  @Test
  public void testFilterByProgress() {
    List<Task> tasks = taskService.searchTasks(Progress.FIFTY);
    List<Task> fiftyTasks = getJobs().stream()
        .filter(job -> job.getProgress().equals(Progress.FIFTY))
        .collect(Collectors.toList());
    assertEquals(fiftyTasks, tasks);
  }

  @Test
  public void testSaveJob() {
    Task j = new Task();
    when(taskRepository.save(j)).thenReturn(j);
    assertEquals(j, taskService.saveTask(j));
  }

}
