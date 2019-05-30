package inc.pabacus.TaskMetrics.desktop.standuply;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.generateToken.TokenRepository;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URL;
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

    String yesterday = yesterdayTextArea.getText();
    String today = todayTextArea.getText();
    String obstacles = obstaclesTextArea.getText();
    StandupService service = new StandupService();
    StandupAnswer answer = service.sendAnswer(new StandupAnswer(yesterday, today, obstacles));
    System.out.println(answer); // heh
    cancel();

  }

  public void cancel() {
    ((Stage) closeButtonAction.getScene().getWindow()).close();
  }

  public void getQuestions(){
    OkHttpClient client = new OkHttpClient();
    // code request code here
    Request request = new Request.Builder()
        .url(HOST + "/api/concierge/questions")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", TokenRepository.getToken().getToken())
        .method("GET", null)
        .build();

    try {
      Response response = client.newCall(request).execute();
      String getQuestions = response.body().string();
      JSONObject obj = new JSONObject(getQuestions);
      JSONArray arr = obj.getJSONArray("questions");

      for (int i = 0; i < arr.length(); ++i) {
        //will check it again after API is serving.
        System.out.println(arr.getString(i));

//        didYesterday.setText(arr.getString(0));
//        doToday.setText(arr.getString(2));
//        obstacles.setText(arr.getString(3));
      }

    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }

  }
}
