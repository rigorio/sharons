package inc.pabacus.TaskMetrics.desktop.settings;

import com.jfoenix.controls.JFXCheckBox;
import inc.pabacus.TaskMetrics.desktop.tracker.AlwaysOnTopCheckerConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPresenter implements Initializable {

  @FXML
  private JFXCheckBox alwaysOnTopCheckbox;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    alwaysOnTopCheckbox.selectedProperty()
        .addListener((observable, oldValue, newValue) -> AlwaysOnTopCheckerConfiguration.setAlwaysOnTop(newValue));
  }
}
