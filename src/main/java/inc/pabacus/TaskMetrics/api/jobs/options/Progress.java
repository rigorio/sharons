package inc.pabacus.TaskMetrics.api.jobs.options;

public enum Progress implements Option{

  ZERO(0),
  TWENTY_FIVE(25),
  FIFTY(50),
  SEVENTY_FIVE(75),
  ONE_HUNDRED(100);

  private Integer progress;

  Progress(Integer progress) {
    this.progress = progress;
  }

  public Integer getProgress() {
    return progress;
  }
}
