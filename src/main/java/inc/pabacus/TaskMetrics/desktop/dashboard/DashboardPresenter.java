package inc.pabacus.TaskMetrics.desktop.dashboard;

import com.jfoenix.controls.JFXButton;
import inc.pabacus.TaskMetrics.desktop.hardware.HardwareView;
import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.desktop.software.SoftwareView;
import inc.pabacus.TaskMetrics.desktop.tasks.TasksView;
import inc.pabacus.TaskMetrics.desktop.timesheet.TimesheetView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardPresenter implements Initializable {

  @FXML
  private AnchorPane dynamicContentPane;
  @FXML
  private JFXButton tasksButton;
  @FXML
  private JFXButton timesheetButton;
  @FXML
  private JFXButton logoutBtn;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    ImageView taskImage = new ImageView(new Image(getClass().getResourceAsStream("/img/jobs.png")));
    setSize(taskImage);
    tasksButton.setGraphic(taskImage);

    ImageView softwareImage = new ImageView(new Image(getClass().getResourceAsStream("/img/timesheet.png")));
    setSize(softwareImage);
    timesheetButton.setGraphic(softwareImage);

    ImageView logoutImage = new ImageView(new Image(getClass().getResourceAsStream("/img/logout.png")));
    setSize(logoutImage);
    logoutBtn.setGraphic(logoutImage);

    viewTasks();
  }

  @FXML
  public void viewTasks() {
    updateDynamicPaneContent(new TasksView().getView());
  }

  @FXML
  public void viewTimesheet() {
    updateDynamicPaneContent(new TimesheetView().getView());
  }

  @FXML
  public void viewSoftware() {
    Parent parent = new SoftwareView().getView();
    updateDynamicPaneContent(parent);
  }

  @FXML
  public void logout() {
    Stage stages = (Stage) logoutBtn.getScene().getWindow();
    stages.close();

    GuiManager.getInstance().changeView(new LoginView());
  }

  private void setSize(ImageView taskImage) {
    taskImage.setFitHeight(30);
    taskImage.setFitWidth(30);
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
