package inc.pabacus.TaskMetrics.desktop.home;

import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePresenter implements Initializable {
  @FXML
  private AnchorPane mainPane;
  @FXML
  private Label countClients;
  @FXML
  private Label countJobs;
  @FXML
  private Label countTasks;
  @FXML
  private Label labelClient;
  @FXML
  private Label labelJobs;
  @FXML
  private Label labelTask;
  @FXML
  private Label nani_kore;

  private JobTaskHandler jobTaskHandler;
  private XpmTaskWebHandler xpmTaskWebHandler;

  public HomePresenter() {
    jobTaskHandler = new JobTaskHandler();
    xpmTaskWebHandler = new XpmTaskWebHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    int numberOfJobs = jobTaskHandler.allJobTasks().size();
    countJobs.setText("" + numberOfJobs);
    int numberOfTasks = xpmTaskWebHandler.findAll().size();
    countTasks.setText("" + numberOfTasks);

  }

  @FXML
  public void hoveringAbove() {
    System.out.println("up above");
    mainPane.setCursor(Cursor.HAND);
  }

  @FXML
  public void viewJobs() {
    System.out.println("watashini jobs");
  }

  @FXML
  public void viewTasks() {
    System.out.println("sore kara tasks");
  }

  @FXML
  public void viewClients() {
    System.out.println("arimasenyo");
  }

  @FXML
  public void unhover() {
    mainPane.setCursor(Cursor.DEFAULT);
  }
}
