package inc.pabacus.TaskMetrics.desktop.timesheet;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLogFXAdapter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TimesheetPresenter implements Initializable {

  @FXML
  private JFXTreeTableView taskSheet;
  @FXML
  private JFXTreeTableView<DailyLogFXAdapter> timeSheet;

  private ObservableList<DailyLogFXAdapter> dailyLogFXAdapters;

  private DailyLogService service = new DailyLogHandler(); // temporary

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    dailyLogFXAdapters = FXCollections.observableArrayList();

    List<DailyLogFXAdapter> logs = service.getAllLogs().stream()
        .map(DailyLogFXAdapter::new)
        .collect(Collectors.toList());

    JFXTreeTableColumn<DailyLogFXAdapter, String> date = new JFXTreeTableColumn<>("DATE");
    date.setCellValueFactory(param -> param.getValue().getValue().getDate());

    JFXTreeTableColumn<DailyLogFXAdapter, String> in = new JFXTreeTableColumn<>("IN");
    in.setCellValueFactory(param -> param.getValue().getValue().getIn());

    JFXTreeTableColumn<DailyLogFXAdapter, String> otl = new JFXTreeTableColumn<>("OTL");
    otl.setCellValueFactory(param -> param.getValue().getValue().getOtl());

    JFXTreeTableColumn<DailyLogFXAdapter, String> bfl = new JFXTreeTableColumn<>("BFL");
    bfl.setCellValueFactory(param -> param.getValue().getValue().getBfl());

    JFXTreeTableColumn<DailyLogFXAdapter, String> out = new JFXTreeTableColumn<>("OUT");
    out.setCellValueFactory(param -> param.getValue().getValue().getOtl());

    timeSheet.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    dailyLogFXAdapters.addAll(logs);

    final TreeItem<DailyLogFXAdapter> root = new RecursiveTreeItem<>(dailyLogFXAdapters, RecursiveTreeObject::getChildren);
    timeSheet.getColumns().addAll(date, in, otl, bfl, out);
    timeSheet.setRoot(root);
    timeSheet.setShowRoot(false);

  }

}
