package inc.pabacus.TaskMetrics.desktop.edit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initEditables();
  }

  public void deleteTask() {
  }

  public void saveTask() {

  }

  private void initEditables() {

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
