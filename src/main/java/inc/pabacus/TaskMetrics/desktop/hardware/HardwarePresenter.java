package inc.pabacus.TaskMetrics.desktop.hardware;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import inc.pabacus.TaskMetrics.api.hardware.HardwareData;
import inc.pabacus.TaskMetrics.api.hardware.HardwareDataFXAdapter;
import inc.pabacus.TaskMetrics.api.hardware.HardwareService;
import inc.pabacus.TaskMetrics.api.hardware.WindowsHardwareHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HardwarePresenter implements Initializable {

  @FXML
  private JFXTreeTableView tableView;

  private HardwareService hardwareService;

  private ObservableList<HardwareDataFXAdapter> hardwares;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hardwareService = new WindowsHardwareHandler();
    hardwares = FXCollections.observableArrayList();

    JFXTreeTableColumn<HardwareDataFXAdapter, String> name = new JFXTreeTableColumn<>("Hardware");
    name.setCellValueFactory(param -> param.getValue().getValue().getName());

    JFXTreeTableColumn<HardwareDataFXAdapter, String> description = new JFXTreeTableColumn<>("Name");
    description.setCellValueFactory(param -> param.getValue().getValue().getDescription());

    name.prefWidthProperty().bind(tableView.widthProperty().divide(2));
    description.prefWidthProperty().bind(tableView.widthProperty().divide(2));

    List<HardwareData> disks = hardwareService.getDisks();
    List<HardwareData> displays = hardwareService.getDisplays();
    disks.forEach(disk->hardwares.add(new HardwareDataFXAdapter(disk)));
    displays.forEach(display->hardwares.add(new HardwareDataFXAdapter(display)));

    final TreeItem<HardwareDataFXAdapter> root = new RecursiveTreeItem<>(hardwares, RecursiveTreeObject::getChildren);
    tableView.getColumns().addAll(name, description);
    tableView.setRoot(root);
    tableView.setShowRoot(false);



  }
}
