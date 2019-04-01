package inc.pabacus.TaskMetrics.api.jobs;

import inc.pabacus.TaskMetrics.api.jobs.options.Progress;
import inc.pabacus.TaskMetrics.api.jobs.options.Status;

import java.util.ArrayList;
import java.util.List;

public class MockJobDataProvider {

  public List<Job> getJobs() {
    List<Job> list = new ArrayList<>();
    list.add(new Job(1L, "KeyWordss", "Description",
                     Status.BACKLOG, Progress.FIFTY,
                     "10/20/19", "Rigo Sarmiento"));
    list.add(new Job(1L, "Job 1", "aaa keywoRD",
                     Status.IN_PROGRESS, Progress.ONE_HUNDRED,
                     "10/20/19", "Rigo Sarmiento"));
    list.add(new Job(1L, "Job 1", "Description",
                     Status.BACKLOG, Progress.SEVENTY_FIVE,
                     "10/20/19", "ASDF asdfKEYWORD asdf"));
    list.add(new Job(0L, "Job 1", "Description",
                     Status.FOR_REVIEW, Progress.FIFTY,
                     "10/20/19", "Rigo Sarmiento"));
    return list;
  }


}
