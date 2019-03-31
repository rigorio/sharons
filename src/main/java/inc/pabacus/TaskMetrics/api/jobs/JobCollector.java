package inc.pabacus.TaskMetrics.api.jobs;

import inc.pabacus.TaskMetrics.api.jobs.options.Option;
import inc.pabacus.TaskMetrics.api.jobs.options.Progress;
import inc.pabacus.TaskMetrics.api.jobs.options.Status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JobCollector implements JobService {

  private JobRepository jobRepository;

  public JobCollector(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  @Override
  public Optional<Job> getJob(Long id) {
    return jobRepository.findById(id);
  }

  @Override
  public List<Job> getAllJobs() {
    return jobRepository.findAll();
  }

  @Override
  public List<Job> searchJobs(String keyword) {
    return jobRepository.findAll().stream()
        .filter(job -> job.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
            job.getDescription().toLowerCase().contains(keyword.toLowerCase()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Job> searchJobs(Status status) {
    return jobRepository.findAll().stream()
        .filter(job -> job.getStatus().getStatus().equals(status.getStatus()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Job> searchJobs(Progress progress) {
    return jobRepository.findAll().stream()
        .filter(job -> job.getProgress().getProgress().equals(progress.getProgress()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Job> filterJobs(Option option, List<Job> jobs) {
    return jobs.stream()
        .filter(job -> option instanceof Status
            ? job.getStatus().getStatus().equals(((Status) option).getStatus())
            : !(option instanceof Progress) || job.getProgress().getProgress().equals(((Progress) option).getProgress()))
        .collect(Collectors.toList());
  }

}
