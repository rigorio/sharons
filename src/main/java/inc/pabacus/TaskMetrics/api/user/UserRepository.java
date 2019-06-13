package inc.pabacus.TaskMetrics.api.user;

public class UserRepository {

  private static String username;
  private static String password;

  public static String getUsername() {
    return username;
  }

  public static void setUsername(String username) {
    UserRepository.username = username;
  }

  public static String getPassword() {
    return password;
  }

  public static void setPassword(String password) {
    UserRepository.password = password;
  }
}
