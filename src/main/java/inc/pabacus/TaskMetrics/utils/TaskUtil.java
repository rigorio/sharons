package inc.pabacus.TaskMetrics.utils;

import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.dto.TaskEditDTO;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.Job;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.TaskTemplate;
import inc.pabacus.TaskMetrics.desktop.jobs.JobTaskIdHolder;

import java.util.Optional;

public class TaskUtil {

  public static TaskEditDTO convertTaskToEditDTO(Task task) {
    JobTaskHandler jobTaskHandler = new JobTaskHandler();
    String jobName = task.getJob();
    Optional<Job> anyJob = jobTaskHandler.allJobs(true).stream()
        .filter(job -> job.getJob().equals(jobName))
        .findAny();

    Job job = anyJob.get();
    Optional<TaskTemplate> anyTask = jobTaskHandler.allTasks().stream()
        .filter(t -> t.getTask().equalsIgnoreCase(t.getTask()) &&
            t.getJobId().equals(job.getId()))
        .findAny();
    TaskTemplate taskTemplate = anyTask.get();
    return TaskEditDTO.builder()
        .id(task.getId()) // entity framework works a bit differently with ids
        .clientId(job.getClientId())
        .jobId(job.getId())
        .description(task.getDescription())
        .taskId(taskTemplate.getId())
        .status(task.getStatus())
        .dateCreated(task.getDateCreated())
        .totalTimeSpent(task.getTotalTimeSpent())
        .startTime(task.getStartTime())
        .endTime(task.getEndTime())
        .dateFinished(task.getDateFinished())
        .billable(task.getBillable())
        .estimateTime(task.getEstimateTime())
        .percentCompleted(task.getPercentCompleted())
        .jobTasksId(JobTaskIdHolder.getId())
//        .businessValueId(task.getBusinessValueId())
//        .invoiceTypeId(task.getInvoiceTypeId().getId())
//        .assigneeId(task.getAssigneeId().getId())
        .build();
  }
}
