package inc.pabacus.TaskMetrics.api.jobs;

import inc.pabacus.TaskMetrics.api.jobs.options.Progress;
import inc.pabacus.TaskMetrics.api.jobs.options.Status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JobHandler implements JobService {

  private JobRepository jobRepository;

  public JobHandler(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  @Override
  public Job saveJob(Job job) {
    return jobRepository.save(job);
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
            job.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
            job.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Job> searchJobs(Status status) {
    return jobRepository.findAll().stream()
        .filter(job -> job.getStatus().equals(Status.BACKLOG))
        .collect(Collectors.toList());
  }

  @Override
  public List<Job> searchJobs(Progress progress) {
    return jobRepository.findAll().stream()
        .filter(job -> job.getProgress().equals(Progress.FIFTY))
        .collect(Collectors.toList());
  }

}
