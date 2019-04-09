package inc.pabacus.TaskMetrics.desktop.software;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import inc.pabacus.TaskMetrics.api.software.SoftwareData;
import inc.pabacus.TaskMetrics.api.software.SoftwareDataFXAdapter;
import inc.pabacus.TaskMetrics.api.software.SoftwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SoftwarePresenter implements Initializable {

  @FXML
  private JFXTreeTableView<SoftwareDataFXAdapter> treeTableView;

  private ObservableList<SoftwareDataFXAdapter> softwares;

  private SoftwareService softwareService;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    softwareService = new SoftwareHandler();
    softwares = FXCollections.observableArrayList();

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> name = new JFXTreeTableColumn<>("Name");
    name.setCellValueFactory(param -> param.getValue().getValue().getName());

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> version = new JFXTreeTableColumn<>("Version");
    version.setCellValueFactory(param -> param.getValue().getValue().getVersion());

    JFXTreeTableColumn<SoftwareDataFXAdapter, String> installedDate = new JFXTreeTableColumn<>("Last Updated");
    installedDate.setCellValueFactory(param -> param.getValue().getValue().getDateInstalled());

    name.prefWidthProperty().bind(treeTableView.widthProperty().divide(2));
    version.prefWidthProperty().bind(treeTableView.widthProperty().divide(4));
    installedDate.prefWidthProperty().bind(treeTableView.widthProperty().divide(4));

    List<SoftwareData> allSoftware = softwareService.getSoftware();

    for (SoftwareData s : allSoftware)
      softwares.add(new SoftwareDataFXAdapter(s));

    final TreeItem<SoftwareDataFXAdapter> root = new RecursiveTreeItem<>(softwares, RecursiveTreeObject::getChildren);
    treeTableView.getColumns().addAll(name, version, installedDate);
    treeTableView.setRoot(root);
    treeTableView.setShowRoot(false);

  }
}
