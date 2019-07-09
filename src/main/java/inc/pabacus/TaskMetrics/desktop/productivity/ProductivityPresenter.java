package inc.pabacus.TaskMetrics.desktop.productivity;

import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.UserActivity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ProductivityPresenter implements Initializable {

  @FXML
  private AnchorPane barPane;
  @FXML
  private PieChart taskPieChart;

  private ActivityHandler activityHandler;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    AnchorPane anchorPane = new AnchorPane();
    anchorPane.setStyle("-fx-background-color:POWDERBLUE");
    barPane.getChildren().add(anchorPane);

    List<UserActivity> activities = activityHandler.all();

    HashMap<String, Double> productivityRecords = new HashMap<>();


    activities.stream()
        .filter(userActivity -> {

          String activity = userActivity.getActivity();
          Double duration = productivityRecords.get(activity);
          if (duration != null)
//            duration += userActivity.get
            productivityRecords.containsKey(activity);
          return true;
        });


  }
}
