package inc.pabacus.TaskMetrics;

import inc.pabacus.TaskMetrics.desktop.login.LoginView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskMetricsApplication extends Application {

  private static final GuiManager MANAGER = GuiManager.getInstance();

  public static void main(String[] args) {
    SpringApplication.run(TaskMetricsApplication.class, args);
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setResizable(false);
    MANAGER.setPrimaryStage(stage);
    MANAGER.showView(new LoginView());
  }

}
