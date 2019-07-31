package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskPostEntity;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Task;
import inc.pabacus.TaskMetrics.desktop.jobs.JobTaskIdHolder;

import java.util.Optional;

public class XpmHelper {

  private JobTaskHandler jobTaskHandler;

  public XpmHelper() {
    jobTaskHandler = new JobTaskHandler();
  }

  public XpmTaskPostEntity helpMe(XpmTask xpmTask) {
    String jobName = xpmTask.getJob();
    Optional<Job> anyJob = jobTaskHandler.allJobs().stream()
        .filter(job -> job.getJob().equals(jobName))
        .findAny();

    Job job = anyJob.get();
    Optional<Task> anyTask = jobTaskHandler.allTasks().stream()
        .filter(task -> task.getTask().equalsIgnoreCase(xpmTask.getTask()) &&
            task.getJobId().equals(job.getId()))
        .findAny();
    Task task = anyTask.get();
    return XpmTaskPostEntity.builder()
        .id(xpmTask.getId()) // entity framework works a bit differently with ids
        .clientId(job.getClientId())
        .jobId(job.getId())
        .description(xpmTask.getDescription())
        .taskId(task.getId())
        .status(xpmTask.getStatus())
        .dateCreated(xpmTask.getDateCreated())
        .totalTimeSpent(xpmTask.getTotalTimeSpent())
        .startTime(xpmTask.getStartTime())
        .endTime(xpmTask.getEndTime())
        .dateFinished(xpmTask.getDateFinished())
        .billable(xpmTask.getBillable())
        .estimateTime(xpmTask.getEstimateTime())
        .percentCompleted(xpmTask.getPercentCompleted())
        .jobTasksId(JobTaskIdHolder.getId())
//        .businessValueId(xpmTask.getBusinessValueId())
//        .invoiceTypeId(xpmTask.getInvoiceTypeId().getId())
//        .assigneeId(xpmTask.getAssigneeId().getId())
        .build();
  }
}
