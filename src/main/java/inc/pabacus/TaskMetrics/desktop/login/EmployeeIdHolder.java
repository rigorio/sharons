package inc.pabacus.TaskMetrics.desktop.login;

public class EmployeeIdHolder {
  private static String employeeId;

  public static String getEmployeeId() {
    return employeeId;
  }

  public static void setEmployeeId(String employeeId) {
    EmployeeIdHolder.employeeId = employeeId;
  }
}
