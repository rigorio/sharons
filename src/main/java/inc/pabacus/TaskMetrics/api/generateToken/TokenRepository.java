package inc.pabacus.TaskMetrics.api.generateToken;

public class TokenRepository {
  private static Token token;

  public static Token getToken() {
    return token;
  }

  public static void setToken(Token token) {
    TokenRepository.token = token;
  }
}
