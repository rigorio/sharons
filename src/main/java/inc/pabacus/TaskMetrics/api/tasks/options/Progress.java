package inc.pabacus.TaskMetrics.api.tasks.options;

public enum Progress implements Option {

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

  public static Progress convert(Integer progress) {
    switch (progress) {
      case 0:
        return ZERO;
      case 25:
        return TWENTY_FIVE;
      case 50:
        return FIFTY;
      case 75:
        return SEVENTY_FIVE;
      case 100:
        return ONE_HUNDRED;
      default:
        return ZERO;
    }
  }
}
