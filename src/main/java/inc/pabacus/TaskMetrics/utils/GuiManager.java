package inc.pabacus.TaskMetrics.utils;

import com.airhacks.afterburner.views.FXMLView;
import inc.pabacus.TaskMetrics.desktop.tracker.TrackHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

  private FXMLView closeWindowDisplayView;

  public void displayView(FXMLView view) {
    closeWindowDisplayView = view;
    Stage stage = new Stage();
    stage.setScene(new Scene(view.getView()));
    stage.show();
  }

  private void closeStageDisplayView() {
    if (closeWindowDisplayView != null){
      TrackHandler.setSelectedTask(null);
      Stage stage = (Stage) closeWindowDisplayView.getView().getScene().getWindow();
      stage.close();
    }
  }

  private double xOffset = 0;
  private double yOffset = 0;

  private void preventClosing() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setAlwaysOnTop(true);
    alert.setTitle("Error");
    alert.setContentText("You're about to close the current running task. You may complete it if you are done.");
    alert.showAndWait();
  }

  private FXMLView closeWindowDisplayWindow;

  public void displayAlwaysOnTop(FXMLView view) {
    //pass view to closeWindow
    closeWindowDisplayWindow = view;

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
    //Prevent from closing
    stage.setOnCloseRequest(evt -> {
      evt.consume();
      preventClosing();
    });
  }

  private void closeStageDisplayOnTop() {
    if (closeWindowDisplayWindow != null){
      TrackHandler.setSelectedTask(null);
      Stage stage = (Stage) closeWindowDisplayWindow.getView().getScene().getWindow();
      stage.close();
    }
  }

  public void closeAllStages(){
    closeStageDisplayView();
    closeStageDisplayOnTop();
  }
}
