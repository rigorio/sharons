package inc.pabacus.TaskMetrics.desktop.productivity;

import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProductivityPresenter implements Initializable {

  @FXML
  private AnchorPane mainPane;
  @FXML
  private PieChart taskPieChart;

  private ActivityHandler activityHandler;
  private XpmTaskWebHandler xpmTaskWebHandler;

  public ProductivityPresenter() {
    xpmTaskWebHandler = BeanManager.xpmTaskHandler();
  }

  private double totalWorkedTime = 0.0;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    xpmTaskWebHandler.findAll()
        .forEach(xpmTask -> {
          if (xpmTask.getDateCreated().equals(LocalDate.now().toString()))
            totalWorkedTime += Double.parseDouble(xpmTask.getTotalTimeSpent());
        });

    double nonWorkingTime = 9.0 - totalWorkedTime;
    System.out.println(totalWorkedTime);
    System.out.println(nonWorkingTime);

    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Working Hours", totalWorkedTime),
            new PieChart.Data("Non Working Hours", nonWorkingTime));
    taskPieChart.setData(pieChartData);
    taskPieChart.setTitle("Productivity information");

    final Label caption = new Label("");
    caption.setTextFill(Color.TEAL);
    caption.setStyle("-fx-font: 24 arial;");

    for (final PieChart.Data data : taskPieChart.getData()) {
      data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                                     e -> {
                                       caption.setTranslateX(e.getSceneX() - 300.0);
                                       caption.setTranslateY(e.getSceneY() - 10.0);
                                       caption.setText(String.format("%.2f", data.getPieValue()) + "%");
                                     });
    }
    mainPane.getChildren().add(caption);
  }
}
