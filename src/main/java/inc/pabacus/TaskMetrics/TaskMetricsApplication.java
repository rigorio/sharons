package inc.pabacus.TaskMetrics;

import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class TaskMetricsApplication extends Application {

  private static final GuiManager MANAGER = GuiManager.getInstance();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setResizable(false);
//    stage.initStyle(StageStyle.TRANSPARENT);
    stage.getIcons().add(new Image("/img/PabacusLogo.png"));
    //Prevent from closing
    stage.setOnCloseRequest(evt -> {
      evt.consume();
      preventFromClosing();
    });
    MANAGER.setPrimaryStage(stage);
    MANAGER.changeView(new LoginView());
  }

  @Override
  public void stop() {
    stopProcesses();
  }

  private void preventFromClosing() {
    Alert alert = createClosePreventionAlert();
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK)
      stopProcesses();
    alert.close();
  }

  private Alert createClosePreventionAlert() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setAlwaysOnTop(true);
    alert.setTitle("You're about to close the application");
    alert.setHeaderText("Warning!");
    alert.setContentText("You're about to close the application! \nPlease save all your work or you will lose your current data data!");
    return alert;
  }

  private void stopProcesses() {
    //to force stop/close the threads.
    Thread.currentThread().interrupt();
    //to make sure app is close
    Platform.setImplicitExit(true);
    Platform.exit();
    System.exit(0);
  }
}
