package inc.pabacus.TaskMetrics.desktop.productivity;

import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.ActivityRecord;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class ProductivityPresenter implements Initializable {

  @FXML
  private JFXComboBox<String> dayCombobox;
  @FXML
  private BarChart barChart;
  @FXML
  private AnchorPane mainPane;
  @FXML
  private PieChart taskPieChart;

  private ActivityHandler activityHandler;
  private XpmTaskWebHandler xpmTaskWebHandler;
  private static final String ALL = "All";
  private static final String L_7 = "Last 7 days";
  private static final String L_90 = "Last 90 days";
  private static final String L_30 = "Last 30 days";
  private static final String TODAY = "Today";
  private List<ActivityRecord> activityRecords;

  public ProductivityPresenter() {
    xpmTaskWebHandler = BeanManager.xpmTaskHandler();
    activityHandler = BeanManager.activityHandler();
  }

  private double totalWorkedTime = 0.0;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    dayCombobox.getItems().setAll(TODAY, L_7, L_30, L_90, ALL);
    dayCombobox.setValue(ALL);
    filterTasks();
//    activityRecords = activityHandler.allRecords();
    setupPieChart();
//    setupBarChart();

  }

  @FXML
  public void filterTasks() {
    List<ActivityRecord> records = activityHandler.allRecords();
    String value = dayCombobox.getValue();
    if (value.equals(ALL)) {
      activityRecords = records;
    } else if (value.equals(TODAY)) {
      activityRecords = records.stream().filter(record -> record.getDate().equals(LocalDate.now().toString()))
          .collect(Collectors.toList());
    } else if (value.equals(L_7)) {

      setRecords(records, 7);
    } else if (value.equals(L_30)) {

      setRecords(records, 30);
    } else if (value.equals(L_90)) {
      setRecords(records, 90);
    }
    setupBarChart();
  }

  private void setRecords(List<ActivityRecord> records, int i) {
    LocalDate today = LocalDate.now();
    LocalDate sevenDaysBefore = today.minus(i, DAYS);
    activityRecords = records.stream()
        .filter(record -> {
          String date = record.getDate();
          LocalDate recordDate = LocalDate.parse(date);

          return (recordDate.isEqual(today) || recordDate.isBefore(today)) && (recordDate.isAfter(sevenDaysBefore));
        })
        .collect(Collectors.toList());
  }

  private void setupBarChart() {
    barChart.getXAxis().labelProperty().setValue("Activities");
    barChart.getYAxis().labelProperty().setValue("Hours Spent");


    XYChart.Series<String, Double> dataSeries1 = new XYChart.Series<>();
    dataSeries1.setName("Hours spent per activity");

    Map<String, Double> valueMap = new HashMap<>();
    for (ActivityRecord activityRecord : activityRecords) {
      String activity = activityRecord.getActivity();
      Double duration = Double.parseDouble(activityRecord.getDuration());
      if (valueMap.containsKey(activity)) {
        Double i1 = valueMap.get(activity);
        duration += i1;
      }
      valueMap.put(activity, duration);
    }

    valueMap.forEach((k, v) -> dataSeries1.getData().add(new XYChart.Data(k, v)));

    barChart.getData().clear();
    barChart.getData().add(dataSeries1);
  }

  private void setupPieChart() {
    xpmTaskWebHandler.findAll()
        .forEach(xpmTask -> {
          if (xpmTask.getDateCreated().equals(LocalDate.now().toString()))
            totalWorkedTime += Double.parseDouble(xpmTask.getTotalTimeSpent());
        });

    double nonWorkingTime = 9.0 - totalWorkedTime;

    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Working Hours - " + percent(totalWorkedTime) + "%", totalWorkedTime),
            new PieChart.Data("Non Working Hours - " + percent(nonWorkingTime) + "%", nonWorkingTime));
    taskPieChart.setData(pieChartData);
    taskPieChart.setTitle("Productivity information");
    taskPieChart.setLegendVisible(false);
    final Label caption = new Label("");
    caption.setTextFill(Color.TEAL);
    caption.setStyle("-fx-font: 24 arial;");

    for (final PieChart.Data data : taskPieChart.getData()) {
      data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                                     e -> {
                                       caption.setTranslateX(e.getSceneX() - 300.0);
                                       caption.setTranslateY(e.getSceneY() - 10.0);
                                       caption.setText(data.getPieValue() + " hours");
                                     });
    }
    mainPane.getChildren().add(caption);

  }

  private double percent(double value) {
    return round(100.0 * (value / 9.0), 2);
  }

  private double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
