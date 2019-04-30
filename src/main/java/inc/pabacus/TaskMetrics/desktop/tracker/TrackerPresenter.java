package inc.pabacus.TaskMetrics.desktop.tracker;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.api.tasks.TaskFXAdapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TrackerPresenter implements Initializable {
  @FXML
  private Label title;
  @FXML
  private Label timer;
  @FXML
  private Label description;
  @FXML
  private JFXButton complete;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    TaskFXAdapter selectedTask = TrackHandler.getSelectedTask();
    System.out.println(selectedTask);
  }
}
