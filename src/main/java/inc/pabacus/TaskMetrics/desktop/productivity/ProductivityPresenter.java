package inc.pabacus.TaskMetrics.desktop.productivity;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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

  public JFXComboBox pieCombobox;

  @FXML
  private AnchorPane mainPane;

  @FXML
  private BarChart barChart;

  @FXML
  private JFXTextArea recommendation;

  @FXML
  private PieChart taskPieChart;

  @FXML
  private JFXComboBox<String> dayCombobox;

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
    recommendation.setEditable(false);
    dayCombobox.getItems().setAll(TODAY, L_7, L_30, L_90, ALL);
    dayCombobox.setValue(ALL);
    filterTasks();
//    activityRecords = activityHandler.allRecords();
//    setupPieChart();
//    setupBarChart();
    newPieChart();
  }


  @FXML
  public void filterTasks() {
    List<ActivityRecord> records = allRecords();
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
    newPieChart();
  }

  @FXML
  public void filterRecords() {
    List<ActivityRecord> activityRecords = allRecords();
  }

  private double totalDuration;

  /**
   * feature for decreasing capacity, algorithm is okay. just need to fill in the blanks
   */
  public void decreasingCapacity() {
    List<ActivityRecord> activityRecords = allRecords();
    for (ActivityRecord activityRecord : activityRecords) {
      if (withinWeek(activityRecord.getDate())) {
        String d = activityRecord.getDuration();
        double duration = Double.parseDouble(d);
        totalDuration += duration;
      }
    }
    double duration = round(totalDuration);
    double percentage = round((duration / availableHours()) / 100.0);
    System.out.println(percentage);
  }

  /*
  Get dates (start, end) from datetime pickers
   */
  private boolean withinWeek(String date) {
    LocalDate recordDate = LocalDate.parse(date);
    LocalDate startDate = recordDate.minus(1, DAYS); // TODO should not be using recordDate
    LocalDate endDate = recordDate.plus(1, DAYS);
    return recordDate.isAfter(startDate) && recordDate.isBefore(endDate);
  }

  /*
  Find out whether or not on leave, and total supposed number of hours
   */
  private double availableHours() {
    return 0.0;
  }

  private String colorHolder;

  private void newPieChart() {

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    Map<String, Double> valueMap = new HashMap<>();
    for (ActivityRecord activityRecord : activityRecords) {
      String activity = activityRecord.getType();
      Double duration = Double.parseDouble(activityRecord.getDuration());
      if (valueMap.containsKey(activity)) {
        Double i1 = valueMap.get(activity);
        duration += i1;
      }
      valueMap.put(activity, duration);
    }
    valueMap.forEach((k, v) -> {
      PieChart.Data pieChart = new PieChart.Data(k, v);
      pieChartData.add(pieChart);
    });

    taskPieChart.setData(pieChartData);
    taskPieChart.setTitle("Performance Insights");
    for (final PieChart.Data data : taskPieChart.getData()) {
      setColors(data);

      data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                                     e -> {
                                       resetColors();
                                       data.getNode().setStyle(
                                           "-fx-pie-color: #1DE9B6;"
                                       );
                                       changeRecommendation(data.getName(), data.getPieValue());
                                     });
    }

  }

  private void setColors(PieChart.Data data) {
    String color = data.getName().equals("Task")
        ? "#FF6F00"
        : data.getName().equals("Meeting")
        ? "#FF8F00"
        : data.getName().equals("Break")
        ? "#FFAB00"
        : data.getName().equals("Training")
        ? "#FFC400"
        : data.getName().equals("Technical Issue")
        ? "#FFD740"
        : data.getName().equals("Field Work")
        ? "#FFD54F"
        : "#FFECB3";
    data.getNode().setStyle("-fx-pie-color: " + color + ";");
  }

  private void changeRecommendation(String type, double pieValue) {
    String accumulated = "You have accumulated " + pieValue + " hours " + type + " for this week.";
    String limit = "";
    if (type.equals("Break")) {
      if (pieValue > 2.5)
        limit = "It is recommended that you only take at most 2.5 hours of breaks per week (two 15-minute breaks each day) to maximize your Productivity.";
      else if (pieValue < 2.5) {
        limit = "Remember you can have two 15-minute breaks each day. Try to balance out work and rest in order to maximize your Productivity!";
      } else
        limit = "You seem to be on track. Nice work! Keep it up and you'll always be able to maximize you Productivity!";
    } else if (type.equals("Meeting")) {
      if (pieValue > 2.5)
        limit = "It is recommended that you only spend at most 2.5 hours in meetings per week (at most 30 minutes each day) to maximize your Productivity.";
      else if (pieValue < 2.5) {
        limit = "Less meetings may mean more work, but remember that these may be vital to correct concerns and realign yourself with the project in order to maximize your Productivity!";
      } else
        limit = "You seem to be on track. Nice work! Keep it up and you'll always be able to maximize you Productivity!";
    } else if (type.equals("Training")) {
      if (pieValue > 2)
        limit = "It is recommended that you only spend 2 hours of training per week to maximize your Productivity.";
      else if (pieValue < 2) {
        limit = "You can spend up to 2 hours training per week. Use this time well in order to maximize your Productivity!";
      } else
        limit = "You seem to be on track. Nice work! Keep it up and you'll always be able to maximize you Productivity!";
    } else if (type.equals("Technical Issue")) {
      limit = "We're sad you experienced some technical issues. Make sure to report them immediately in order to maximize your Productivity!";
    } else if (type.equals("Field Work")) {
      limit = "You spent " + pieValue + " hours out there. Nice work! Keep it up and you'll always be able to maximize you Productivity!";
    } else if (type.equals("Task")) {
      if (pieValue > 37.5)
        limit = "Thank you for your hard work! Be careful to not overwork yourself and avoid a burnout in order to maximize your Productivity!";
      else if (pieValue < 37.5) {
        limit = "It seems you spent less time in productivity this week. bla bla in order to maximize your Productivity.";
      } else
        limit = "You seem to be on track. Nice work! Keep it up and you'll always be able to maximize you Productivity!";
    }
//    String conclusion = "It is recommended to only consume " + limit + " hours per week in order to maximize your Productivity.";


        /*
        Over - You have accumulated X Hrs Productivity for this Week.  Our recommendation is to have at most 2.5hrs (5 30-min in 5 days) meetings in a week to fully maximize your Productivity
Under - You have accumulated X Hrs of Meeting for this Week.  We encourage you to consume the provided time and you'll be amazed by the Team Productivity result!
         */

    recommendation.setText(accumulated + "\n" + limit);
  }

  private void resetColors() {
    for (final PieChart.Data data : taskPieChart.getData()) {
      setColors(data);
    }
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

  private List<ActivityRecord> allRecords() {
    return activityHandler.allRecords();
  }

  private double percent(double value) {
    return round(100.0 * (value / 9.0));
  }

  private double round(double value) {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(2, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
