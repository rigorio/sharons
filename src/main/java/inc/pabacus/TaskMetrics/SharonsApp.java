package inc.pabacus.TaskMetrics;

import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SharonsApp extends Application {

  private static final GuiManager MANAGER = GuiManager.getInstance();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setResizable(true);
    stage.getIcons().add(new Image("/img/PabacusLogo.png"));
    MANAGER.setPrimaryStage(stage);
//    MANAGER.changeView(new LoginView());
  }

  @Override
  public void stop() {
  }


}
