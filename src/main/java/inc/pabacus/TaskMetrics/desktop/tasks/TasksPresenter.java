package inc.pabacus.TaskMetrics.desktop.tasks;

import inc.pabacus.TaskMetrics.api.tasks.Task;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TasksPresenter implements Initializable {

  @FXML
  private TableView tableView;

  private List<Task> tasks = new ArrayList<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("are?");

    tasks.add(new Task("phase 1", "writhing in agony"));
    tasks.add(new Task("phase 2", "death"));
    tableView.setEditable(true);

    TableColumn<Task, String> name = new TableColumn<>("Name");
    name.setSortable(true);
    name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
    name.setCellFactory(column -> new TaskCell());

    TableColumn<Task, String> action = new TableColumn<>("Action");
    action.setCellFactory(param -> new TaskCell());
    tableView.getColumns().addAll(name, action);


  }
}
