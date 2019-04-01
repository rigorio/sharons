package inc.pabacus.TaskMetrics.api.jobs;

import inc.pabacus.TaskMetrics.api.jobs.options.Progress;
import inc.pabacus.TaskMetrics.api.jobs.options.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobHandlerTest extends MockJobDataProvider {

  private JobService jobService;
  private JobRepository jobRepository;

  @Before
  public void init() {
    jobRepository = mock(JobRepository.class);
    jobService = new JobHandler(jobRepository);
  }

  @Test
  public void testFindAll() {
    List<Job> list = new ArrayList<>();
    Job job = new Job(1L, "Job 1", "Description",
                      Status.BACKLOG, Progress.SEVENTY_FIVE,
                      "10/20/19", "Rigo Sarmiento");
    list.add(job);

    when(jobRepository.findAll()).thenReturn(list);
    List<Job> allJobs = jobService.getAllJobs();

    assertEquals(list, allJobs);
  }

  @Test
  public void testSearchWithKeyword() {
    List<Job> list = getJobs();

    when(jobRepository.findAll()).thenReturn(list);

    List<Job> jobs = jobService.searchJobs("keyword");
    list.removeIf(job -> job.getId().equals(0L));

    assertEquals(list, jobs);
  }

  @Test
  public void testFilterByStatus() {

  }
}
