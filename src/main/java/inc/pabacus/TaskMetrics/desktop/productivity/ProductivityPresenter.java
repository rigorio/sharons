package inc.pabacus.TaskMetrics.desktop.productivity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductivityPresenter implements Initializable {

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
  }
}
