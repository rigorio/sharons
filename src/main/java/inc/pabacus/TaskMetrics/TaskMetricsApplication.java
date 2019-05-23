package inc.pabacus.TaskMetrics;

import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

//@SpringBootApplication
public class TaskMetricsApplication extends Application {

  private static final GuiManager MANAGER = GuiManager.getInstance();
  private StandupService standupService = new StandupService();

  private void preventFromClosing() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setAlwaysOnTop(true);
    alert.setTitle("You're about to close the application");
    alert.setHeaderText("Warning!");
    alert.setContentText("You're about to close the application! \nPlease make sure there are no other window current opened or you will lost your data!");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){
      //to force stop/close the threads.
      Thread.currentThread().interrupt();
      //to make sure app is close
      Platform.setImplicitExit(true);
      Platform.exit();
      System.exit(0);
    } else {
      alert.close();
    }
  }

  public static void main(String[] args) {
//    SpringApplication.run(TaskMetricsApplication.class, args);
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setResizable(false);
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
    standupService.close();
    System.exit(0); // this is a hackly hack
  }
}
