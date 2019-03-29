package inc.pabacus.TaskMetrics.api.jobs;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JobCollector {

  private JobRepository jobRepository;

  public JobCollector(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

  public Optional<Job> getJob(Long id) {
    return jobRepository.findById(id);
  }

  public List<Job> getAllJobs() {
    return jobRepository.findAll();
  }

  public List<Job> searchJobs(String keyword) {
    return jobRepository.findAll().stream()
        .filter(job -> job.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
            job.getDescription().toLowerCase().contains(keyword.toLowerCase()))
        .collect(Collectors.toList());
  }

  public List<Job> searchJobs(Status status) {
    return jobRepository.findAll().stream()
        .filter(job -> job.getStatus().getStatus().equals(status.getStatus()))
        .collect(Collectors.toList());
  }

  public List<Job> searchJobs(Progress progress) {
    return jobRepository.findAll().stream()
        .filter(job -> job.getProgress().getProgress().equals(progress.getProgress()))
        .collect(Collectors.toList());
  }

}
