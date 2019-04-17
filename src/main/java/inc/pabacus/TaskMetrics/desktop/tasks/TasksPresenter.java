package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXTextArea;
import inc.pabacus.TaskMetrics.utils.TimerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class TasksPresenter implements Initializable {

  private static final List<String> STATUS = new ArrayList<>(Arrays.asList("Backlog", "In Progress", "For Review", "Closed"));

  @FXML
  private TableView<ToDo> backLogsTable;
  @FXML
  private TableColumn<ToDo, String> colToDo;
  @FXML
  private TableColumn<ToDo, String> backlogTime;


  @FXML
  private TableView<InProgress> tableViewInProgress;
  @FXML
  private TableColumn<ToDo, String> progressTime;
  @FXML
  private TableColumn<ToDo, String> colInProgress;

  @FXML
  private TableView<Done> tableViewDone;
  @FXML
  private TableColumn<ToDo, String> colDone;
  @FXML
  private TableColumn<ToDo, String> totalTime;

  @FXML
  private JFXTextArea hiddenLabelTask;
  @FXML
  private Label timerLabel;
  @FXML
  private Label timerLabelFx;
  @FXML
  private Label currentStatusLabel;
  private TimerService service = new TimerService();

  private ObservableList<ToDo> backlogs = FXCollections.observableArrayList(
      new ToDo("Design the App", "Designing of the app", "Backlog", "00:00:00"),
      new ToDo("Run the App", "Designing of the app", "Backlog", "00:00:00"),
      new ToDo("Make a timer", "Designing of the app", "Backlog", "00:00:00"),
      new ToDo("Make a Java Class", "Designing of the app", "Backlog", "00:00:00")
  );


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    colToDo.setCellValueFactory(new PropertyValueFactory<>("title"));
    backlogTime.setCellValueFactory(new PropertyValueFactory<>("timeSpent"));
    backlogTime.setResizable(false);
    colToDo.setResizable(false);

    colInProgress.setCellValueFactory(new PropertyValueFactory<>("InProgress"));
    progressTime.setCellValueFactory(new PropertyValueFactory<>("timeSpent"));

    colDone.setCellValueFactory(new PropertyValueFactory<>("Done"));
    totalTime.setCellValueFactory(new PropertyValueFactory<>("timeSpent"));

    backLogsTable.setItems(backlogs);
    tableViewInProgress.setItems(observableListInProgress);
    tableViewDone.setItems(observableListDone);
    timerLabel.setText("00:00:00");
    hiddenLabelTask.setVisible(false);
  }

  @FXML
  public void changeStatus() {
    ToDo selectedItem = backLogsTable.getSelectionModel().getSelectedItem();
    String currentStatus = selectedItem.getStatus().get();
    String newStatus = changeStatus(currentStatus);
    selectedItem.setStatus(newStatus);
    // save selectedItem
    refreshTasks();
  }

  @FXML
  public void refreshTasks() {
    backLogsTable.getItems().clear(); // does this clear the data??
  }

  private String changeStatus(String status) {
    return "mock me";
  }

  private String moveLeft(String status) {
    int i = STATUS.indexOf(status);
    return STATUS.get(i - 1);
  }

  private String moveRight(String status) {
    int i = STATUS.indexOf(status);
    return STATUS.get(i + 1);
  }


  public void timerFxStart() {
    Runnable process = () -> {
      long duration = service.getTime();
      String time = service.formatSeconds(duration);
      timerLabel.setText(time);
    };
    service.setFxProcess(process);
    service.start();
  }
  // dummy data

  ObservableList<InProgress> observableListInProgress = FXCollections.observableArrayList(
  );

  ObservableList<Done> observableListDone = FXCollections.observableArrayList(
  );


  public void moveToDo(ActionEvent event) {
    timerFxStart();
    try {
      ToDo toDos = backLogsTable.getSelectionModel().getSelectedItem();
      hiddenLabelTask.setText(toDos.getTitle());

      backLogsTable.getItems().removeAll(backLogsTable.getSelectionModel().getSelectedItem());

      InProgress inProgress = new InProgress(hiddenLabelTask.getText());
      tableViewInProgress.getItems().add(inProgress);
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public void moveInProgress(ActionEvent event) {
    service.pause();
    try {
      InProgress inProgress = tableViewInProgress.getSelectionModel().getSelectedItem();
      hiddenLabelTask.setText(inProgress.getInProgress() + " - Time: " + timerLabel.getText());

      tableViewInProgress.getItems().removeAll(tableViewInProgress.getSelectionModel().getSelectedItem());

      Done done = new Done(hiddenLabelTask.getText());
      tableViewDone.getItems().add(done);
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public void backInProgress(ActionEvent event) {
    timerFxStart();
    try {
      Done done = tableViewDone.getSelectionModel().getSelectedItem();
      hiddenLabelTask.setText(done.getDone());
      String doneLabel = hiddenLabelTask.getText();
      String[] parts = doneLabel.split("-");
      String getPart = parts[0];
      hiddenLabelTask.setText(getPart);

      tableViewDone.getItems().removeAll(tableViewDone.getSelectionModel().getSelectedItem());


      InProgress inProgress = new InProgress(hiddenLabelTask.getText());
      tableViewInProgress.getItems().add(inProgress);
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public void backToDo(ActionEvent event) {
    service.pause();
    service.reset();
    timerLabel.setText("00:00:00");
    try {
      InProgress inProgress = tableViewInProgress.getSelectionModel().getSelectedItem();
      hiddenLabelTask.setText(inProgress.getInProgress());

      tableViewInProgress.getItems().removeAll(tableViewInProgress.getSelectionModel().getSelectedItem());


//      ToDo todo = new ToDo(hiddenLabelTask.getText());
//      backLogsTable.getItems().add(todo);
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  @FXML
  void onHandleChangeStatus(ActionEvent event) {
    List<String> choices = new ArrayList<>();
    choices.add("Log In");
    choices.add("Morning Break");
    choices.add("Out to Lunch");
    choices.add("Back from Lunch");
    choices.add("Afternoon Break");
    choices.add("Log Out");
    choices.add("Meeting");
    choices.add("Training");


    ChoiceDialog<String> dialog = new ChoiceDialog<>(currentStatusLabel.getText(), choices);
    Image imageImage1 = new Image("/img/status.png", 50, 50, false, false);
    ImageView imageView = new ImageView(imageImage1);
    dialog.setGraphic(imageView);
    dialog.setTitle("Status");
    dialog.setHeaderText("Please choose your status");
    dialog.setContentText("Choose your status:");

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      currentStatusLabel.setText(result.get());
    }


  }

  public void newTask(ActionEvent event) {
    TextInputDialog dialog = new TextInputDialog("");
    Image imageImage1 = new Image("/img/task.png", 50, 50, false, false);
    ImageView imageView = new ImageView(imageImage1);
    dialog.setGraphic(imageView);
//    dialog.initOwner(stage);


//    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
//    stage.getIcons().add(new Image(this.getClass().getResource("/img/PabacusLogo.png").toString()));

    dialog.setTitle("New Task:");
    dialog.setHeaderText("Add a new Task");
    dialog.setContentText("Enter a new Task:");
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
//      ToDo todo = new ToDo(result.get());
//      backLogsTable.getItems().add(todo);
    }
  }

  public void viewScreenShot(ActionEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ScreenShot.fxml"));
      Parent root1 = (Parent) fxmlLoader.load();
      Stage stage = new Stage();
      stage.getIcons().add(new Image("/img/PabacusLogo.png"));
      stage.setTitle("Screen Shots");
      stage.setScene(new Scene(root1));
      stage.show();

    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
