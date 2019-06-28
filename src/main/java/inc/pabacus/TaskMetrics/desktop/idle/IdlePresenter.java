package inc.pabacus.TaskMetrics.desktop.idle;

import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.UserActivity;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class IdlePresenter implements Initializable {

  @FXML
  private JFXComboBox<String> actionsBox;

  private ActivityHandler activityHandler;


  private String startTime;

  private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    activityHandler = new ActivityHandler();
    startTime = timeFormatter.format(LocalTime.now().minus(5, ChronoUnit.MINUTES));
    UserActivity userActivity = UserActivity.builder()
        .time(startTime)
        .date(LocalDate.now().toString())
        .activity(Activity.IDLE.getActivity())
        .build();
    activityHandler.saveActivity(userActivity);

    List<String> options = new ArrayList<>();
    options.add("Meeting");
    options.add("Coffee Break");
    options.add("Bathroom Break");
    options.add("Technical issue");
    actionsBox.setItems(FXCollections.observableArrayList(options));

  }

  @FXML
  public void submitActivity() {

    ActivityListener activityListener = BeanManager.activityListener();
    activityListener.listen();


    String activity = actionsBox.getValue();

    UserActivity userActivity = UserActivity.builder()
        .time(timeFormatter.format(LocalTime.now()))
        .date(LocalDate.now().toString())
        .activity(Activity.BUSY.getActivity())
        .build();
    activityHandler.saveActivity(userActivity);

    Stage stage = (Stage) actionsBox.getScene().getWindow();
    stage.close();
  }

}
