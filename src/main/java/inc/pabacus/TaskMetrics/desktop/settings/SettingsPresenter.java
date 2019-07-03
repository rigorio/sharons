package inc.pabacus.TaskMetrics.desktop.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.desktop.tracker.AlwaysOnTopCheckerConfiguration;
import inc.pabacus.TaskMetrics.desktop.tracker.CountdownTimerConfiguration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPresenter implements Initializable {

  @FXML
  private JFXCheckBox alwaysOnTopCheckbox;
  @FXML
  private JFXCheckBox countdownTimer;
  @FXML
  private JFXTextField extendField;

  @FXML
  private JFXButton extendButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    alwaysOnTop();
    countdownTimer();
    getDefaultExtend();
  }

  private void getDefaultExtend() {
    try{
    if (ExtendConfiguration.getExtendMinutes().isEmpty()){
      ExtendConfiguration.setExtendMinutes("15");
      extendField.setText("15");
    }
    else
      extendField.setText(ExtendConfiguration.getExtendMinutes());
    }
    catch (Exception e){
      //to make sure set the extend minutes
      extendField.setText("15");
      ExtendConfiguration.setExtendMinutes("15");
    }
  }

  @FXML
  private void extendButton() {
    if (!isEmpty())
      ExtendConfiguration.setExtendMinutes(extendField.getText());
    else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Empty");
      alert.setContentText("Please do not leave the textfield empty.");
      alert.showAndWait();
      extendField.setText("15");
    }
  }

  private void alwaysOnTop() {
    if (AlwaysOnTopCheckerConfiguration.isAlwaysOnTop())
      alwaysOnTopCheckbox.setSelected(true);
    else
      alwaysOnTopCheckbox.setSelected(false);

    alwaysOnTopCheckbox.selectedProperty()
        .addListener((observable, oldValue, newValue) -> AlwaysOnTopCheckerConfiguration.setAlwaysOnTop(newValue));
  }

  private void countdownTimer() {
    if (CountdownTimerConfiguration.isCountdownTimer())
      countdownTimer.setSelected(true);
    else
      countdownTimer.setSelected(false);

    countdownTimer.selectedProperty()
        .addListener((observable, oldValue, newValue) -> CountdownTimerConfiguration.setCountdownTimer(newValue));
  }

  private boolean isEmpty() {
    return extendField.getText().isEmpty();
  }
}
