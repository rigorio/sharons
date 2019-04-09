package inc.pabacus.TaskMetrics.desktop.tasks;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import inc.pabacus.TaskMetrics.api.tasks.Task;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class TaskCell extends TableCell<Task, String> {

  private HBox hBox = new HBox();
  private JFXButton complete = new JFXButton();
  private JFXButton start = new JFXButton();

  public TaskCell() {
    hBox.setStyle("-fx-pref-height: 50px");

    FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
    icon.setFill(Color.valueOf("#5497ce"));
    complete.setGraphic(icon);
    complete.setStyle("-fx-background-color: #FFFFFF; -fx-cursor: hand");

    complete.setOnAction(evt -> {
      getTableView().getSelectionModel().select(getIndex());
      Long id = getTableView().getSelectionModel().getSelectedItem().getId(); // wow what
//      getTableView().getSelectionModel().getSelectedItem().complete();
    });

    // also add an action to start

    FontAwesomeIconView playIcon = new FontAwesomeIconView(FontAwesomeIcon.PLAY_CIRCLE);
    playIcon.setFill(Color.valueOf("#5497ce"));
    start.setGraphic(playIcon);
    start.setStyle("-fx-background-color: #FFFFFF; -fx-cursor: hand");

    hBox.getChildren().addAll(complete, start);
    hBox.setAlignment(Pos.CENTER);
    hBox.setSpacing(10.0);
  }

  @Override
  public void updateSelected(boolean selected) {
    super.updateSelected(selected);
  }
}
