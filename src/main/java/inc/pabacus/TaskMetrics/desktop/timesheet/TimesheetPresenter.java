package inc.pabacus.TaskMetrics.desktop.timesheet;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.TaskFXAdapter;
import inc.pabacus.TaskMetrics.api.tasks.TaskLog;
import inc.pabacus.TaskMetrics.api.tasks.WorkLog;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogHandler;
import inc.pabacus.TaskMetrics.api.timesheet.DailyLogService;
import inc.pabacus.TaskMetrics.api.timesheet.logs.DailyLogFXAdapter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

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
  private JFXTreeTableView taskSheet;
  @FXML
  private JFXTreeTableView<DailyLogFXAdapter> timeSheet;

  private ObservableList<DailyLogFXAdapter> dailyLogFXAdapters;

  private ObservableList<TaskFXAdapter> taskFXAdapters;

  private DailyLogService service = new DailyLogHandler(); // temporary

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    initTimeSheet();
    initTaskSheet();

  }

  private void initTaskSheet() {
    taskFXAdapters = FXCollections.observableArrayList();

    List<TaskLog> taskLogs = new ArrayList<>();
    List<Task> tasks = new ArrayList<>();
    List<TreeTableColumn> wow = new ArrayList<>();
    for (TaskLog taskLog : taskLogs) {
      JFXTreeTableColumn date = new JFXTreeTableColumn<>(taskLog.getDate().toString());
      List<String> taskTitles = new ArrayList<>();
      for (String day : past5Days()) {
        taskTitles = tasks.stream()
            .filter(task -> task.getWorkLogs().stream()
                .filter(ifDateIsToday(day))
                .collect(Collectors.toList())
                .isEmpty())
            .map(Task::getTitle)
            .collect(Collectors.toList());

      }
      date.getColumns().addAll(taskTitles);
      wow.add(date);
    }

//    final TreeItem<DailyLogFXAdapter> root = new RecursiveTreeItem<>(dailyLogFXAdapters, RecursiveTreeObject::getChildren);
    taskSheet.getColumns().addAll(wow);
//    taskSheet.setRoot(root);
    taskSheet.setShowRoot(false);
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

}
