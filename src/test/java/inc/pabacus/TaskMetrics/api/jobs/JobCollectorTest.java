package inc.pabacus.TaskMetrics.api.jobs;

import org.junit.Before;

import static org.mockito.Mockito.mock;

public class JobCollectorTest {

  private JobService jobService;

  @Before
  public void init() {
    JobRepository jobRepository = mock(JobRepository.class);
    jobService = new JobCollector(jobRepository);
  }

}
