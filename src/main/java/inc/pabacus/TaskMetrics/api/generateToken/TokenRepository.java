package inc.pabacus.TaskMetrics.api.generateToken;

import org.springframework.data.jpa.repository.JpaRepository;

public class TokenRepository {
  private static Token token;

  public static Token getTokens() {
    return token;
  }

  public static void setTokens(Token token) {
    TokenRepository.token = token;
  }
}
