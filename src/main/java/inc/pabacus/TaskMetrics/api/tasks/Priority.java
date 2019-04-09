package inc.pabacus.TaskMetrics.api.tasks;

public enum Priority {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5);

  private Integer priority;

  Priority(Integer priority) {
    this.priority = priority;
  }

  public Integer getPriority() {
    return priority;
  }
}
