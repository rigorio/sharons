package inc.pabacus.TaskMetrics.desktop.productivity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductivityPresenter implements Initializable {

  @FXML
  private StackedBarChart<String, Number> stackedBarChart;
  @FXML
  private PieChart taskPieChart;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Productive", 80),
            new PieChart.Data("Meetings", 5),
            new PieChart.Data("Breaks", 10),
            new PieChart.Data("Idle", 5));
    taskPieChart.setData(pieChartData);
    taskPieChart.setTitle("Productivity Chart");


    final CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Bars");
    final NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Value");

    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    series1.setName("1800");
    series1.getData().add(new XYChart.Data<>("Productive", 80));
    series1.getData().add(new XYChart.Data<>("Idle", 15));
    series1.getData().add(new XYChart.Data<>("Productive", 57));
    series1.getData().add(new XYChart.Data<>("Idle", 23));
    series1.getData().add(new XYChart.Data<>("Productive", 36));
    series1.getData().add(new XYChart.Data<>("Productive", 80));
    series1.getData().add(new XYChart.Data<>("Productive", 52));
    series1.getData().add(new XYChart.Data<>("Productive", 80));
    series1.getData().add(new XYChart.Data<>("Idle", 5));
    series1.getData().add(new XYChart.Data<>("Productive", 80));
    series1.getData().add(new XYChart.Data<>("Productive", 52));


//    stackedBarChart = new StackedBarChart<>(xAxis, yAxis);

//    new XYChart.Series<>(xAxis, yAxis);
    stackedBarChart.setTitle("Productivity Graph");

  }
}
