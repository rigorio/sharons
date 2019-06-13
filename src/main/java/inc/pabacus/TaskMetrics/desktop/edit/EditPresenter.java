package inc.pabacus.TaskMetrics.desktop.edit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskWebHandler;
import inc.pabacus.TaskMetrics.api.tasks.XpmTask;
import inc.pabacus.TaskMetrics.api.tasks.XpmTaskAdapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditPresenter implements Initializable {

  @FXML
  private JFXTextField titleText;
  @FXML
  private JFXComboBox progressText;
  @FXML
  private JFXComboBox statusText;
  @FXML
  private JFXComboBox priorityText;
  @FXML
  private JFXTextArea descriptionText;
  @FXML
  private JFXButton deleteTask;
  @FXML
  private JFXButton saveTask;

  private XpmTask task;

  private XpmTaskWebHandler xpmTaskWebHandler;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    xpmTaskWebHandler = new XpmTaskWebHandler();
    XpmTaskAdapter xpmTask = EditableTaskHolder.getTask();
    task = new XpmTask(xpmTask);
    initEditables();
  }

  public void deleteTask() {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Delete task?");
    alert.setContentText("Task #" + task.getId());

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      xpmTaskWebHandler.deleteById(task.getId());
      close();
    }
  }

  public void saveTask() {
    task.setTitle(titleText.getText());
    task.setDescription(descriptionText.getText());
    xpmTaskWebHandler.save(task);
    close();
  }

  private void close() {
    EditableTaskHolder.setTask(null);
    Stage stage = (Stage) statusText.getScene().getWindow();
    stage.close();
  }

  private void initEditables() {

    titleText.setText(task.getTitle());
    descriptionText.setText(task.getDescription());

    progressText.getItems().addAll(new ArrayList<String>() {{
      add("0");
      add("25");
      add("50");
      add("75");
      add("100");
    }});

    ArrayList<String> statuses = new ArrayList<String>() {{
      add("Pending");
      add("In Progress");
      add("Done");
    }};
    statusText.getItems().addAll(statuses);
    statuses.add("All");
    statusText.getItems().addAll(statuses);


    priorityText.getItems().addAll(new ArrayList<String>() {{
      add("1");
      add("2");
      add("3");
      add("4");
      add("5");
    }});
  }

}
