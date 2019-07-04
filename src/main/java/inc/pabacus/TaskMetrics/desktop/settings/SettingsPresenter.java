package inc.pabacus.TaskMetrics.desktop.settings;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import inc.pabacus.TaskMetrics.api.hardware.HardwareData;
import inc.pabacus.TaskMetrics.api.hardware.HardwareDataFXAdapter;
import inc.pabacus.TaskMetrics.api.hardware.HardwareService;
import inc.pabacus.TaskMetrics.api.hardware.WindowsHardwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareData;
import inc.pabacus.TaskMetrics.api.software.SoftwareDataFXAdapter;
import inc.pabacus.TaskMetrics.api.software.SoftwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareService;
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
import javafx.scene.control.TreeItem;
import lombok.Data;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("all")
public class SettingsPresenter implements Initializable {

  @FXML
  private JFXTreeTableView<SoftwareDataFXAdapter> softwareTable;
  @FXML
  private JFXTreeTableView<HardwareDataFXAdapter> hardwareTable;
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
    initSoftware();
    initHardware();
    alwaysOnTop();
    countdownTimer();
    getDefaultExtend();
    managerBox.getItems().addAll(populateManagers());
    TableColumn<String, String> managers = new TableColumn("Managers");
    managers.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
    managerTable.getColumns().addAll(managers);
  }

  private void initHardware() {
    HardwareService hardwareService = new WindowsHardwareHandler();
    ObservableList<HardwareDataFXAdapter> hardwares = FXCollections.observableArrayList();

    JFXTreeTableColumn<HardwareDataFXAdapter, String> name = new JFXTreeTableColumn<>("Hardware");
    name.setCellValueFactory(param -> param.getValue().getValue().getName());

    JFXTreeTableColumn<HardwareDataFXAdapter, String> description = new JFXTreeTableColumn<>("Name");
    description.setCellValueFactory(param -> param.getValue().getValue().getDescription());

    name.prefWidthProperty().bind(hardwareTable.widthProperty().divide(2));
    description.prefWidthProperty().bind(hardwareTable.widthProperty().divide(2));

    List<HardwareData> disks = hardwareService.getDisks();
    List<HardwareData> displays = hardwareService.getDisplays();
    List<HardwareData> usbDevices = hardwareService.getUsbDevices();
    disks.forEach(disk -> hardwares.add(new HardwareDataFXAdapter(disk)));
    displays.forEach(display -> hardwares.add(new HardwareDataFXAdapter(display)));
    usbDevices.forEach(usbDevice -> hardwares.add(new HardwareDataFXAdapter(usbDevice)));

    final TreeItem<HardwareDataFXAdapter> root = new RecursiveTreeItem<>(hardwares, RecursiveTreeObject::getChildren);
    hardwareTable.getColumns().addAll(name, description);
    hardwareTable.setRoot(root);
    hardwareTable.setShowRoot(false);

  }

  private void initSoftware() {
    SoftwareService softwareService = new SoftwareHandler();
    ObservableList<SoftwareDataFXAdapter> softwares = FXCollections.observableArrayList();

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> name = new JFXTreeTableColumn<>("Name");
    name.setCellValueFactory(param -> param.getValue().getValue().getName());

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> version = new JFXTreeTableColumn<>("Version");
    version.setCellValueFactory(param -> param.getValue().getValue().getVersion());

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> installedDate = new JFXTreeTableColumn<>("Last Updated");
    installedDate.setCellValueFactory(param -> param.getValue().getValue().getDateInstalled());

    name.prefWidthProperty().bind(softwareTable.widthProperty().divide(2));
    version.prefWidthProperty().bind(softwareTable.widthProperty().divide(4));
    installedDate.prefWidthProperty().bind(softwareTable.widthProperty().divide(4));

    List<SoftwareData> allSoftware = softwareService.getSoftware();

    for (SoftwareData s : allSoftware)
      softwares.add(new SoftwareDataFXAdapter(s));

    final TreeItem<SoftwareDataFXAdapter> root = new RecursiveTreeItem<>(softwares, RecursiveTreeObject::getChildren);
    softwareTable.getColumns().addAll(name, version, installedDate);
    softwareTable.setRoot(root);
    softwareTable.setShowRoot(false);
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
