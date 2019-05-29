package inc.pabacus.TaskMetrics;

import inc.pabacus.TaskMetrics.api.standuply.StandupService;
import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//@SpringBootApplication
public class TaskMetricsApplication extends Application {

  private static final GuiManager MANAGER = GuiManager.getInstance();
  private StandupService standupService = new StandupService();

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

  @Override
  public void stop() {
    standupService.close();
    System.exit(0); // this is a hackly hack
  }
}
