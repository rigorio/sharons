package inc.pabacus.TaskMetrics.desktop.timesheet;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import inc.pabacus.TaskMetrics.api.hardware.WindowsHardwareHandler;
import inc.pabacus.TaskMetrics.api.software.SoftwareHandler;
import inc.pabacus.TaskMetrics.api.tasks.TaskFXAdapter;
import inc.pabacus.TaskMetrics.api.tasks.WorkLog;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLogFXAdapter;
import inc.pabacus.TaskMetrics.desktop.hardware.HardwarePresenter;
import inc.pabacus.TaskMetrics.desktop.hardware.HardwareView;
import inc.pabacus.TaskMetrics.desktop.software.SoftwareView;
import inc.pabacus.TaskMetrics.utils.GuiManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TimesheetPresenter implements Initializable {

  @FXML
  private ImageView softwareImg;
  @FXML
  private ImageView hardwareImg;
  @FXML
  private Label os;
  @FXML
  private Label hardware;
  @FXML
  private JFXTreeTableView taskSheet;
  @FXML
  private JFXTreeTableView<DailyLogFXAdapter> timeSheet;

  private ObservableList<DailyLogFXAdapter> dailyLogFXAdapters;

  private ObservableList<TaskFXAdapter> taskFXAdapters;

  private DailyLogService service = new DailyLogHandler(); // temporary
  private static final int DEF_SIZE = 900;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    softwareImg.setImage(new Image("/img/software.png", DEF_SIZE, DEF_SIZE, false, true));
    hardwareImg.setImage(new Image("/img/hardware.png", DEF_SIZE, DEF_SIZE, false, true));

    os.setText(new SoftwareHandler().getOs());
    hardware.setText(new WindowsHardwareHandler().getAllInfo().getProcessor().getName());

    initTimeSheet();
  }

  @FXML
  public void viewHardware() {
    GuiManager.getInstance().displayView(new HardwareView());
  }

  @FXML
  public void viewSoftware() {
    GuiManager.getInstance().displayView(new SoftwareView());
  }

  private void initTimeSheet() {
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

  private Predicate<WorkLog> ifDateIsToday(String date) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    return workLog -> {
      try {
        return formatter.parse(workLog.getDate()).equals(formatter.parse(date));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return false;
    };
  }

  private List<String> past5Days() {
    List<String> past5Days = new ArrayList<>();
    past5Days.add(LocalDate.now().toString());
    past5Days.add(LocalDate.now().minus(1, ChronoUnit.DAYS).toString());
    past5Days.add(LocalDate.now().minus(2, ChronoUnit.DAYS).toString());
    past5Days.add(LocalDate.now().minus(3, ChronoUnit.DAYS).toString());
    past5Days.add(LocalDate.now().minus(4, ChronoUnit.DAYS).toString());
    return past5Days;
  }

}
