package inc.pabacus.TaskMetrics.api.user;

public class UserRepository {

  private static Username username;
  private static Password password;

  public static Username getUsername() {
    return username;
  }

  public static void setUsername(Username username) {
    UserRepository.username = username;
  }

  public static Password getPassword() {
    return password;
  }

  public static void setPassword(Password password) {
    UserRepository.password = password;
  }
}
