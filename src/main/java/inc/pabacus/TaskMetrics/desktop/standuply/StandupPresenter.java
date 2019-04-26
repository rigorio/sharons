package inc.pabacus.TaskMetrics.desktop.standuply;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.standuply.StandupAnswer;
import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class StanduplyPresenter implements Initializable {

  @FXML
  private TextArea yesterdayTextArea;
  @FXML
  private TextArea todayTextArea;
  @FXML
  private TextArea obstaclesTextArea;

  @FXML
  private JFXButton closeButtonAction;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  public void submitForm() {

    String yesterday = yesterdayTextArea.getText();
    String today = todayTextArea.getText();
    String obstacles = obstaclesTextArea.getText();
    StandupService service = new StandupService();
    StandupAnswer answer = service.sendAnswer(new StandupAnswer(yesterday, today, obstacles));
    System.out.println(answer);

  }

  public void cancel() {
    ((Stage) closeButtonAction.getScene().getWindow()).close();
  }
}
