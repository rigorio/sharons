package inc.pabacus.TaskMetrics.desktop.standuply;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;


import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class StanduplyPresenter implements Initializable {

  @FXML
  private JFXButton closeButtonAction;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  public void closeButtonAction(javafx.event.ActionEvent event) {
    Stage stage = (Stage) closeButtonAction.getScene().getWindow();
    // do what you have to do
    stage.close();
  }
}
