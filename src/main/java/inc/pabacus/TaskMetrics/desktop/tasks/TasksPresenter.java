package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

import javax.xml.transform.Result;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class TasksPresenter implements Initializable {

  @FXML
  public TableView<ToDo> tableViewToDo;
  @FXML
  public TableColumn<ToDo, String> colToDo;
  @FXML
  private TableView<InProgress> tableViewInProgress;
  @FXML
  private TableColumn<ToDo, String> colInProgress;
  @FXML
  public TableView<Done> tableViewDone;
  @FXML
  public TableColumn<ToDo, String> colDone;
  @FXML
  private JFXTextArea hiddenLabelTask;
  @FXML
  Label timerLabel;
  @FXML
  Label timerLabelFx;
  @FXML
  public Label currentStatusLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    colToDo.setCellValueFactory(new PropertyValueFactory<>("ToDo"));
    colInProgress.setCellValueFactory(new PropertyValueFactory<>("InProgress"));
    colDone.setCellValueFactory(new PropertyValueFactory<>("Done"));
    tableViewToDo.setItems(observableListToDo);
    tableViewInProgress.setItems(observableListInProgress);
    tableViewDone.setItems(observableListDone);
    timerLabel.setText("00:00:00");
    hiddenLabelTask.setVisible(false);
  }
  private TimerService service = new TimerService();

  public void timerFxStart() {
    Runnable process = () -> {
      long duration = service.getTime();
      String time = service.formatSeconds(duration);
      timerLabel.setText(time);
    };
    service.setProcess(process);
    service.start();
  }

  // dummy data
  ObservableList<ToDo> observableListToDo = FXCollections.observableArrayList(
          new ToDo("Design the App"),
          new ToDo("Run the App"),
          new ToDo("Make a timer"),
          new ToDo("Make a Java Class")
  );

  ObservableList<InProgress> observableListInProgress = FXCollections.observableArrayList(
  );

  ObservableList<Done> observableListDone = FXCollections.observableArrayList(
  );


  public void moveToDo(ActionEvent event) {
    timerFxStart();
    try{
    ToDo toDos = tableViewToDo.getSelectionModel().getSelectedItem();
      hiddenLabelTask.setText(toDos.getToDo());

    tableViewToDo.getItems().removeAll(tableViewToDo.getSelectionModel().getSelectedItem());

    InProgress inProgress = new InProgress(hiddenLabelTask.getText());
    tableViewInProgress.getItems().add(inProgress);
    }
    catch (Exception e){
      System.out.println(e);
    }

  }

  public void moveInProgress(ActionEvent event) {
    service.pause();
    try{
      InProgress inProgress = tableViewInProgress.getSelectionModel().getSelectedItem();
      hiddenLabelTask.setText(inProgress.getInProgress() + " - Time: " + timerLabel.getText());

      tableViewInProgress.getItems().removeAll(tableViewInProgress.getSelectionModel().getSelectedItem());

      Done done = new Done(hiddenLabelTask.getText());
      tableViewDone.getItems().add(done);
    }
    catch (Exception e){
      System.out.println(e);
    }

  }

  public void backInProgress(ActionEvent event) {
    timerFxStart();
    try{
      Done done = tableViewDone.getSelectionModel().getSelectedItem();
      hiddenLabelTask.setText(done.getDone());
      String doneLabel = hiddenLabelTask.getText();
      String[] parts = doneLabel.split("-");
      String getPart = parts[0];
      hiddenLabelTask.setText(getPart);

      tableViewDone.getItems().removeAll(tableViewDone.getSelectionModel().getSelectedItem());


      InProgress inProgress = new InProgress(hiddenLabelTask.getText());
      tableViewInProgress.getItems().add(inProgress);
    }
    catch (Exception e){
      System.out.println(e);
    }

  }

  public void backToDo(ActionEvent event) {
    service.pause();
    service.reset();
    timerLabel.setText("00:00:00");
    try{
      InProgress inProgress = tableViewInProgress.getSelectionModel().getSelectedItem();
      hiddenLabelTask.setText(inProgress.getInProgress());

      tableViewInProgress.getItems().removeAll(tableViewInProgress.getSelectionModel().getSelectedItem());


      ToDo todo = new ToDo(hiddenLabelTask.getText());
      tableViewToDo.getItems().add(todo);
    }
    catch (Exception e){
      System.out.println(e);
    }

  }

  @FXML
  void onHandleChangeStatus(ActionEvent event){
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
    Image imageImage1 = new Image("/img/status.png", 50,50,false,false);
    ImageView imageView = new ImageView(imageImage1);
    dialog.setGraphic(imageView);
    dialog.setTitle("Status");
    dialog.setHeaderText("Please choose your status");
    dialog.setContentText("Choose your status:");

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()){
        currentStatusLabel.setText(result.get());
    }



  }

  public void newTask(ActionEvent event) {
    TextInputDialog dialog = new TextInputDialog("");
    Image imageImage1 = new Image("/img/task.png", 50,50,false,false);
    ImageView imageView = new ImageView(imageImage1);
    dialog.setGraphic(imageView);
//    dialog.initOwner(stage);


//    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
//    stage.getIcons().add(new Image(this.getClass().getResource("/img/PabacusLogo.png").toString()));

    dialog.setTitle("New Task:");
    dialog.setHeaderText("Add a new Task");
    dialog.setContentText("Enter a new Task:");
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()){
      ToDo todo = new ToDo(result.get());
      tableViewToDo.getItems().add(todo);
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

    } catch (Exception e){
      System.out.println(e);
    }
  }
}
