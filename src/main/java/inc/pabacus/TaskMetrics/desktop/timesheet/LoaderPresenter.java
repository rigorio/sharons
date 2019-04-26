package inc.pabacus.TaskMetrics.desktop.timesheet;

import inc.pabacus.TaskMetrics.desktop.hardware.HardwareView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoaderPresenter implements Initializable {

  @FXML
  private StackPane rootPane;

  @FXML
  private ImageView imageView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    imageView.setImage(new Image("/img/load.gif"));
    new LoaderScreen().start();
  }
  class LoaderScreen extends Thread{
    public void run(){
      try {
        Thread.sleep(2000);

        Platform.runLater(new Runnable() {
          @Override
          public void run() {


            Parent root1 = null;
            try {
              root1 = FXMLLoader.load(getClass().getResource("../software/software.fxml"));
            } catch (IOException e) {
              e.printStackTrace();
            }
            PauseTransition wait = new PauseTransition(Duration.seconds(0.2));
            Parent finalRoot = root1;
            wait.setOnFinished((e) -> {
              Stage stage = new Stage();
              stage.getIcons().add(new Image("/img/PabacusLogo.png"));
              stage.initModality(Modality.NONE);
              stage.setScene(new Scene(finalRoot));
              stage.show();
            });
            wait.play();


            rootPane.getScene().getWindow().hide();

          }
        });


      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
