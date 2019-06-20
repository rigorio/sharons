package inc.pabacus.TaskMetrics.api.chat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;

@Data
@Entity
@Builder
@NoArgsConstructor
public class Chat {
  private String message;
  private String time;

  public Chat(String message, String time) {
    this.message = message;
    this.time = time;
  }
}
