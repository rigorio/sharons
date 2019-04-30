package inc.pabacus.TaskMetrics.utils;

import com.airhacks.afterburner.views.FXMLView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

  public void changeView(FXMLView view) {
    primaryStage.setScene(new Scene(view.getView()));
    primaryStage.show();
  }

  public void displayView(FXMLView view) {
    Stage stage = new Stage();
    stage.setScene(new Scene(view.getView()));
    stage.show();
  }

  private double xOffset = 0;
  private double yOffset = 0;

  public void displayAlwaysOnTop(FXMLView view) {
    Stage stage = new Stage();

    Scene scene = new Scene(view.getView());
    scene.setOnMousePressed(event -> {
      xOffset = event.getSceneX();
      yOffset = event.getSceneY();
    });

    //move around here
    scene.setOnMouseDragged(event -> {
      stage.setX(event.getScreenX() - xOffset);
      stage.setY(event.getScreenY() - yOffset);
    });

    stage.initStyle(StageStyle.UNDECORATED);
    stage.setScene(scene);
    stage.setAlwaysOnTop(true);
    stage.setResizable(false);
    stage.show();
  }

}
