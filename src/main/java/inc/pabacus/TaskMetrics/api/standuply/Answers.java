package inc.pabacus.TaskMetrics.api.standuply;

import javax.persistence.Entity;

@Entity
public class Answers {
  private String question;
  private String answer;

  public Answers() {
  }

  public Answers(String question, String answer) {
    this.question = question;
    this.answer = answer;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}
