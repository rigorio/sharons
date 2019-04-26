package inc.pabacus.TaskMetrics;

import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

//@SpringBootApplication
public class TaskMetricsApplication extends Application {

  private static final GuiManager MANAGER = GuiManager.getInstance();

  public static void main(String[] args) {
//    SpringApplication.run(TaskMetricsApplication.class, args);
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setResizable(false);
    stage.getIcons().add(new Image("/img/PabacusLogo.png"));
    MANAGER.setPrimaryStage(stage);
    MANAGER.changeView(new LoginView());
  }
}
