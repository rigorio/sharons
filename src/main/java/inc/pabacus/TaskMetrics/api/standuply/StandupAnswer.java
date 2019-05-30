package inc.pabacus.TaskMetrics.api.standuply;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
public class StandupAnswer {
  private Long userId;
  private Long questionId;
  @OneToMany(cascade = CascadeType.ALL)
  private List<Answers> answers;

  public StandupAnswer(Long userId, Long questionId, List<Answers> answers) {
    this.userId = userId;
    this.questionId = questionId;
    this.answers = answers;
  }
}
