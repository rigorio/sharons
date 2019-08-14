package inc.pabacus.TaskMetrics.desktop.home;

import inc.pabacus.TaskMetrics.api.client.ClientHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.jobTask.JobTaskHandler;
import inc.pabacus.TaskMetrics.desktop.jobs.JobsView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePresenter implements Initializable {
  @FXML
  private AnchorPane dynamicContentPane;
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
  private Label information;

  private JobTaskHandler jobTaskHandler;
  private XpmTaskWebHandler xpmTaskWebHandler;
  private ClientHandler clientHandler;

  public HomePresenter() {
    jobTaskHandler = new JobTaskHandler();
    xpmTaskWebHandler = new XpmTaskWebHandler();
    clientHandler = new ClientHandler();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    int numberOfJobs = jobTaskHandler.allJobTasks().size();
    countJobs.setText("" + numberOfJobs);
    int numberOfTasks = xpmTaskWebHandler.findAll().size();
    countTasks.setText("" + numberOfTasks);
    int numberOfClients = clientHandler.allClients().size();
    countClients.setText("" + numberOfClients);
    gatherInfo();
  }

  @FXML
  public void hoveringAbove() {
    System.out.println("up above");
    dynamicContentPane.setCursor(Cursor.HAND);
  }

  @FXML
  public void viewJobs() {
    updateDynamicPaneContent(new JobsView().getView());
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
    dynamicContentPane.setCursor(Cursor.DEFAULT);
  }

  private void gatherInfo() {
    information.setText("Dynamic information report regarding jobs and tasks");
  }

  private void updateDynamicPaneContent(Parent parent) {
    AnchorPane.setTopAnchor(parent, 0.0);
    AnchorPane.setLeftAnchor(parent, 0.0);
    AnchorPane.setBottomAnchor(parent, 0.0);
    AnchorPane.setRightAnchor(parent, 0.0);

    dynamicContentPane.getChildren().clear();
    dynamicContentPane.getChildren().add(parent);
  }
}
