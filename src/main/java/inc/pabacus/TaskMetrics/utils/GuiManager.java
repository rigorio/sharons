package inc.pabacus.TaskMetrics.utils;

import com.airhacks.afterburner.views.FXMLView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiManager {

  private static final GuiManager INSTANCE = new GuiManager();

  private Stage primaryStage;

  private GuiManager() {
  }

  public static GuiManager getInstance() {
    return INSTANCE;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void showView(FXMLView view) {
    primaryStage.setScene(new Scene(view.getView()));
    primaryStage.show();
  }

  public void displayView(FXMLView view) {
    Stage stage = new Stage();
    stage.setScene(new Scene(view.getView()));
    stage.show();
  }

}
