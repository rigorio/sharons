package inc.pabacus.TaskMetrics.desktop.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.desktop.tracker.AlwaysOnTopCheckerConfiguration;
import inc.pabacus.TaskMetrics.desktop.tracker.CountdownTimerConfiguration;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Data;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsPresenter implements Initializable {

  @FXML
  private TableView managerTable;
  @FXML
  private JFXComboBox<String> managerBox;
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
    managerBox.getItems().addAll(populateManagers());
    TableColumn<String, String> managers = new TableColumn("Managers");
    managers.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
    managerTable.getColumns().addAll(managers);
  }

  private void getDefaultExtend() {
    try {
      if (ExtendConfiguration.getExtendMinutes().isEmpty()) {
        ExtendConfiguration.setExtendMinutes("15");
        extendField.setText("15");
      } else
        extendField.setText(ExtendConfiguration.getExtendMinutes());
    } catch (Exception e) {
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

  @FXML
  public void addManager() {
    String manager = managerBox.getValue();
//    managerTable.setItems();
  }

  @FXML
  public void removeManager() {
  }

  public ObservableList<String> populateManagers() {
    List<String> names = new ArrayList<>();
    names.add("Joy Cuison");
    names.add("Mae Cayabyab");
    names.add("Rigo Sarmiento");
    names.add("Carlo Montemayor");
    names.add("Chris Nebril");
    names.add("Gabrielle Floresca");
    names.add("Edmond Balingit");
    return FXCollections.observableArrayList(names);
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

  @Data
  private class Person {
    StringProperty name;
  }
}
