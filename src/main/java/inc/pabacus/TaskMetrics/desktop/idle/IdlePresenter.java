package inc.pabacus.TaskMetrics.desktop.idle;

import com.jfoenix.controls.JFXComboBox;
import inc.pabacus.TaskMetrics.api.activity.Activity;
import inc.pabacus.TaskMetrics.api.activity.ActivityHandler;
import inc.pabacus.TaskMetrics.api.activity.ActivityTimestamp;
import inc.pabacus.TaskMetrics.api.listener.ActivityListener;
import inc.pabacus.TaskMetrics.utils.BeanManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
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

    Platform.runLater(this::onCloseRequest);

  }

  @FXML
  public void submitActivity() {
    String action = actionsBox.getValue();
    if (action == null) {
      onNull();
      return;
    } else if (!action.equals(TECHNICAL_ISSUE)) {

      ActivityTimestamp startActivity = ActivityTimestamp.builder()
          .time(startTime)
          .date(LocalDate.now().toString())
          .build();
      startActivity.setActivity(Activity.convert(action).getActivity());
      activityHandler.saveTimestamp(startActivity); // initial activity upon detecting idle

      activityHandler.saveTimestamp(Activity.BUSY); // update if not busy
    } else {
      // show a dialogue that let's staff report technical issue
    }

    ActivityListener activityListener = BeanManager.activityListener();
//    activityListener.listen();

    Stage stage = (Stage) actionsBox.getScene().getWindow();
    stage.close();
  }

  private void onCloseRequest() {
    Stage stage = (Stage) actionsBox.getScene().getWindow();
    stage.setOnCloseRequest(evt -> {
      evt.consume();
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Not allowed");
      alert.setContentText("You're not allowed to close this window. Please update your status!");
      alert.showAndWait();
    });
  }

  private void onNull() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Not allowed");
    alert.setContentText("Please submit your status");
    alert.showAndWait();
  }

}
