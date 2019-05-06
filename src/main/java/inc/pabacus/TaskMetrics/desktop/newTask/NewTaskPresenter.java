package inc.pabacus.TaskMetrics.desktop.newTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import inc.pabacus.TaskMetrics.api.project.Project;
import inc.pabacus.TaskMetrics.api.project.ProjectHandler;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import inc.pabacus.TaskMetrics.api.tasks.TaskHandler;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValue;
import inc.pabacus.TaskMetrics.api.tasks.businessValue.BusinessValueHandler;
import inc.pabacus.TaskMetrics.api.tasks.options.Status;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import okhttp3.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NewTaskPresenter implements Initializable {

  @FXML
  private JFXTextField descriptionField;

  @FXML
  private JFXComboBox<String> billableComboBox;

  @FXML
  private JFXButton saveButton;

  @FXML
  private JFXButton closeButton;

  @FXML
  private JFXComboBox<String> projectComboBox;

  @FXML
  private JFXComboBox<String> businessComboBox;

  ObservableList<String> billableList = FXCollections.observableArrayList("True", "False");

  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static final String HOST = "http://localhost:8080";
  private BusinessValueHandler businessValueHandler = new BusinessValueHandler();
  private ProjectHandler projectHandler = new ProjectHandler();
  private TaskHandler taskHandler = new TaskHandler();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    //unfocus textfield
    Platform.runLater(() -> closeButton.requestFocus()); // what

    List<String> businesses = getAllBusinessValues().stream()
        .map(BusinessValue::getBusiness)
        .collect(Collectors.toList());
    businessComboBox.getItems().addAll(businesses);
    billableComboBox.setPromptText("Choose Billable");
    projectComboBox.setPromptText("Choose Project");
    businessComboBox.setPromptText("Choose Business Value");
    List<String> projects = getAllProjects().stream()
        .map(Project::getProjectName)
        .collect(Collectors.toList());
    projectComboBox.setItems(FXCollections.observableArrayList(projects));
    billableComboBox.setItems(billableList);
    // set default value
    projectComboBox.setValue("ABC");
    billableComboBox.setValue("True");
    businessComboBox.setValue("Accounting");
    }

  @FXML
  public void close() {
    Stage stage = (Stage) projectComboBox.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void save() {
    boolean isMprojectComboBoxEmpty = projectComboBox.getSelectionModel().isEmpty();
    boolean isbillableComboBoxEmpty = billableComboBox.getSelectionModel().isEmpty();
    boolean isbusinessComboBoxEmpty = businessComboBox.getSelectionModel().isEmpty();
    boolean isDescriptionEmpty = descriptionField.getText().isEmpty();

    if (isMprojectComboBoxEmpty || isbillableComboBoxEmpty || isDescriptionEmpty || isbusinessComboBoxEmpty) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Error");
      alert.setContentText("Please fill out all the fields");
      alert.showAndWait();
    } else {
      String title = descriptionField.getText(); // actually title of task
      Boolean billable = Boolean.valueOf(billableComboBox.getValue());

      BusinessValue businessValue = getBusinessValue();
      Project project = getProject();

      Task task = Task.builder()
          .title(title)
          .projectId(project.getId())
          .projectName(project.getProjectName())
          .businessValueId(businessValue.getId())
          .billable(billable)
          .timeSpent(0.0)
          .status(Status.BACKLOG)
          .build();

      Task t = taskHandler.createTask(task);
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setContentText("Task saved!");
      alert.showAndWait();
      Stage stage = (Stage) projectComboBox.getScene().getWindow();
      stage.close();
    }
  }

  private Project getProject() {
    Optional<Project> anyProject = getAllProjects().stream()
        .filter(project -> project.getProjectName().equals(projectComboBox.getValue()))
        .findAny();

    if (!anyProject.isPresent())
      throw new RuntimeException("No Project was found");

    return anyProject.get();
  }

  private BusinessValue getBusinessValue() {
    Optional<BusinessValue> any = getAllBusinessValues().stream()
        .filter(businessValue -> businessValue.getBusiness().equals(businessComboBox.getValue()))
        .findAny();

    if (!any.isPresent())
      throw new RuntimeException("No business value found"); // throw dialog box

    return any.get();
  }

  private List<BusinessValue> getAllBusinessValues() {
    return businessValueHandler.getAll();
  }

  private List<Project> getAllProjects() {
    return projectHandler.getAllProjects();
  }
}
