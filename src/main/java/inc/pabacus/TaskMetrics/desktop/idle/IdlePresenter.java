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
  private static final String TECHNICAL_ISSUE = "Technical issue";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    activityHandler = BeanManager.activityHandler();
    startTime = timeFormatter.format(LocalTime.now().minus(5, ChronoUnit.MINUTES));
    List<String> options = new ArrayList<>();
    options.add("Meeting");
    options.add("Break");
    options.add(TECHNICAL_ISSUE);
    actionsBox.setItems(FXCollections.observableArrayList(options));

  }

  @FXML
  public void submitActivity() {

    UserActivity startActivity = UserActivity.builder()
        .time(startTime)
        .date(LocalDate.now().toString())
        .build();

    ActivityListener activityListener = BeanManager.activityListener();
    activityListener.listen();


    String action = actionsBox.getValue();
    Activity activity = action.equals(TECHNICAL_ISSUE)
        ? Activity.BUSY
        : Activity.convert(action);

    startActivity.setActivity(activity.getActivity());
    activityHandler.saveActivity(startActivity); // initial activity upon detecting idle
    if (activity != Activity.BUSY)
      activityHandler.saveActivity(Activity.BUSY); // update if not busy

    Stage stage = (Stage) actionsBox.getScene().getWindow();
    stage.close();
  }

}
