package inc.pabacus.TaskMetrics.api.jobs;

import inc.pabacus.TaskMetrics.api.jobs.options.Option;
import inc.pabacus.TaskMetrics.api.jobs.options.Progress;
import inc.pabacus.TaskMetrics.api.jobs.options.Status;

import java.util.List;
import java.util.Optional;

public interface JobService {
  Optional<Job> getJob(Long id);

  List<Job> getAllJobs();

  List<Job> searchJobs(String keyword);

  List<Job> searchJobs(Status status);

  List<Job> searchJobs(Progress progress);

  List<Job> filterJobs(Option option, List<Job> jobs);
}
