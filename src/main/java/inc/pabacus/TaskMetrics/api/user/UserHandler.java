package inc.pabacus.TaskMetrics.api.user;

public class UserHandler {

  public String getUsername() {
    return UserRepository.getUsername();
  }

  public String getPassword() {
    return UserRepository.getPassword();
  }

  public void setUsername(String username) {
    UserRepository.setUsername(username);
  }

  public void setPassword(String password) {
    UserRepository.setPassword(password);
  }
}
