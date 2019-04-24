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

  public static Priority convert(Integer priority) {
    switch (priority) {
      case 1:
        return ONE;
      case 2:
        return TWO;
      case 3:
        return THREE;
      case 4:
        return FOUR;
      case 5:
        return FIVE;
      default:
        return null;
    }
  }
}
