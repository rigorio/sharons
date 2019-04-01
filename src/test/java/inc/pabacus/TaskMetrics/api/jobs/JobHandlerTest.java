package inc.pabacus.TaskMetrics.api.jobs;

import inc.pabacus.TaskMetrics.api.jobs.options.Progress;
import inc.pabacus.TaskMetrics.api.jobs.options.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobHandlerTest extends MockJobDataProvider {

  private JobService jobService;
  private JobRepository jobRepository;
  private List<Job> list = getJobs();

  @Before
  public void init() {
    jobRepository = mock(JobRepository.class);
    jobService = new JobHandler(jobRepository);
    when(jobRepository.findAll()).thenReturn(list);
  }

  @Test
  public void testFindAll() {
    List<Job> allJobs = jobService.getAllJobs();
    assertEquals(list, allJobs);
  }

  @Test
  public void testSearchWithKeyword() {
    List<Job> jobs = jobService.searchJobs("keyword");
    list.removeIf(job -> job.getId().equals(0L));
    assertEquals(list, jobs);
  }

  @Test
  public void testFilterByStatus() {
    List<Job> jobs = jobService.searchJobs(Status.BACKLOG);
    List<Job> backLogJobs = getJobs().stream()
        .filter(job -> job.getStatus().equals(Status.BACKLOG))
        .collect(Collectors.toList());
    assertEquals(backLogJobs, jobs);
  }

  @Test
  public void testFilterByProgress() {
    List<Job> jobs = jobService.searchJobs(Progress.FIFTY);
    List<Job> fiftyJobs = getJobs().stream()
        .filter(job -> job.getProgress().equals(Progress.FIFTY))
        .collect(Collectors.toList());
    assertEquals(fiftyJobs, jobs);
  }
}
