package inc.pabacus.TaskMetrics.desktop.standuply;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
import inc.pabacus.TaskMetrics.api.standuply.Answers;
import inc.pabacus.TaskMetrics.api.standuply.StandupAnswer;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class StandupPresenter implements Initializable {

  @FXML
  private TextArea yesterdayTextArea;
  @FXML
  private TextArea todayTextArea;
  @FXML
  private TextArea obstaclesTextArea;
  @FXML
  private JFXButton closeButtonAction;
  @FXML
  private Label didYesterday;
  @FXML
  private Label obstacles;
  @FXML
  private Label doToday;

  private static final String HOST = "http://localhost:8080";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    getQuestions();
  }

  public void submitForm() {

    String yesterdayQuestion = didYesterday.getText();
    String yesterdayAnswer = yesterdayTextArea.getText();
    String todayQuestion = doToday.getText();
    String todayAnswer = todayTextArea.getText();
    String obstacleQuestion = obstacles.getText();
    String obstacleAnswer = obstaclesTextArea.getText();

    List<Answers> getallAnswers = Arrays.asList(
        new Answers(yesterdayQuestion,yesterdayAnswer),
        new Answers(todayQuestion,todayAnswer),
        new Answers(obstacleQuestion,obstacleAnswer)
                                               );
    StandupService service = new StandupService();
    StandupAnswer answers = service.sendAnswer(new StandupAnswer(3L, getallAnswers));
    cancel();

  }

  public void cancel() {
    ((Stage) closeButtonAction.getScene().getWindow()).close();
  }

  public void getQuestions(){
    OkHttpClient client = new OkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/Questions")
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", TokenRepository.getToken().getToken())
        .method("GET", null)
        .build();

    try {
      Response response = client.newCall(request).execute();
      String getQuestions = response.body().string();

      if(getQuestions.equals("[]") || getQuestions.equals("")){
        // default value of Questions
        didYesterday.setText("What did you do yesterday? Answer!");
        doToday.setText("What is your plan today?");
        obstacles.setText("Do you have obstacles?");
      }
      else {
        String getTimeJson = getQuestions.replaceAll("\\[|\\]", "");
        JSONObject json = new JSONObject(getTimeJson);
        didYesterday.setText(json.getString("questionDidYesterday"));
        doToday.setText(json.getString("questionDoingToday"));
        obstacles.setText(json.getString("questionObstacles"));
      }

    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }

  }
}
