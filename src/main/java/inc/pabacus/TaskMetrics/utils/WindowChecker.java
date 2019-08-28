package inc.pabacus.TaskMetrics.utils;

public class WindowChecker {

  private static boolean newTaskWindowOpen = false;

  public static boolean isNewTaskWindowOpen() {
    return newTaskWindowOpen;
  }

  public static void setNewTaskWindowOpen(boolean newTaskWindowOpen) {
    WindowChecker.newTaskWindowOpen = newTaskWindowOpen;
  }
}
