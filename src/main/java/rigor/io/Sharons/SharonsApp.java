package rigor.io.Sharons;

import rigor.io.Sharons.dashboard.DashboardView;
import rigor.io.Sharons.utils.GuiManager;
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
//    stage.getIcons().add(new Image("/img/.png"));
    MANAGER.setPrimaryStage(stage);
    MANAGER.changeView(new DashboardView());
//    MANAGER.changeView(new LoginView());
  }

  @Override
  public void stop() {
  }


}
